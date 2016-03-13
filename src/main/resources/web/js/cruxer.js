/* Cruxer Engine */

var CRUXER = (function(CRUXER, $, B) {

    "use strict";

    function getMetaCsrf() {
        var tokenElement = document.querySelector('meta[name="csrf-token"]');
        var headerElement = document.querySelector('meta[name="csrf-header"]');

        return {
            token: tokenElement && tokenElement.getAttribute("content"),
            header: headerElement && headerElement.getAttribute("content")
        };
    };

    function urlFile(url) {
        //URL Tools - http://befused.com/javascript/get-filename-url

        //this removes the anchor at the end, if there is one
        url = url.substring(0, (url.indexOf("#") == -1) ? url.length : url.indexOf("#"));

        //this removes the query after the file name, if there is one
        url = url.substring(0, (url.indexOf("?") == -1) ? url.length : url.indexOf("?"));

        //this removes everything before the last slash in the path
        return url.substring(url.lastIndexOf("/") + 1, url.length);
    };

    function urlRoot(url) {
        //URL Tools - http://befused.com/javascript/get-filename-url
        return url.substring(0, url.lastIndexOf("/") + 1);
    };

    function placeOn(instance, pickInfo) {
        var surface = pickInfo.pickedMesh,
            position = pickInfo.pickedPoint,
            normal = pickInfo.getNormal();

        // Update Cruxer Variables
        instance.cruxer.position = position;
        instance.cruxer.spin = instance.cruxer.spin || 0;
        instance.cruxer.rotAxis = B.Vector3.Cross(B.Axis.Y, normal).normalize();
        instance.cruxer.rotAngle = Math.acos(B.Vector3.Dot(normal, B.Axis.Y));
        instance.cruxer.surface = surface;

        // Reset Rotation
        if (instance.rotationQuaternion) {
            instance.rotationQuaternion.x = 0;
            instance.rotationQuaternion.y = 0;
            instance.rotationQuaternion.z = 0;
            instance.rotationQuaternion.w = 1;
        }

        // Update Babylon Variables
        instance.parent = instance.cruxer.surface;
        instance.setAbsolutePosition(instance.cruxer.position);
        instance.rotate(instance.cruxer.rotAxis, instance.cruxer.rotAngle);
        instance.rotate(B.Axis.Y, instance.cruxer.spin, B.Space.LOCAL);
    };

    function collides(sourceType, targetType) {
        if (sourceType === "holds" && targetType === "walls") {
            return true;
        } else if (sourceType === "walls" && targetType === "floors") {
            return true;
        } else if (sourceType === "decor" && targetType === "floors") {
            return true;
        } else if (sourceType === "decor" && targetType === "walls") {
            return true;
        } else {
            return false
        }
    };

    CRUXER.Engine = function(canvas) {
        // Babylon Engine
        // enableOfflineSupport - http://www.html5gamedevs.com/topic/13548-is-it-possible-to-stop-the-manifest-error/
        this.engine = new B.Engine(canvas, true);
        this.engine.enableOfflineSupport = false;

        // Babylon Scene
        this.scene = new B.Scene(this.engine);
        this.scene.clearColor = B.Color3.FromHexString("#FFFFFF");

        // Babylon Camera
        this.camera = new B.FreeCamera("camera", new BABYLON.Vector3(0, 25, 100), this.scene);
        this.camera.setTarget(B.Vector3.Zero());
        this.camera.attachControl(canvas, false, false);
        this.scene.activeCamera = this.camera;

        // Babylon Assets
        // defaultLoadingScreen does not return control to camera after loading
        // assets tasks need to be reset to avoid being rerun
        this.assets = new B.AssetsManager(this.scene);
        this.assets.useDefaultLoadingScreen = false;
        this.assets.onFinish = function(tasks) {
            if (tasks !== null) { this.reset(); }
        };

        // Canvas Bindings
        canvas.addEventListener('ontouchstart', function(e) { e.preventDefault() });
        canvas.addEventListener('ontouchmove', function(e) { e.preventDefault() });
        canvas.addEventListener('contextmenu', function(e) { e.preventDefault() });
        canvas.addEventListener('mousewheel', function(e) { e.preventDefault() });

        // State Variables
        this._exportId = 0;
        this._id = null;
        this._type = null;
        this._mesh = null;
        this._flag = 0;
        this._prevX = 0;
        this._prevY = 0;

        // TODO : Canvas bindings are a bit of a mess.

        $(canvas).mousedown(function(event) {
            this._flag = 1;
            this._prevX = event.pageX;
            this._prevY = event.pageY;
        }.bind(this));

        $(canvas).mousemove(function(event) {
            if (this._flag === 0) {
                if (this._mesh) {
                    this.meshMove(this._mesh);
                }
            } else if (this._flag === 1) {
                var dx = event.pageX - this._prevX;
                var dy = event.pageY - this._prevY;
                var d = Math.sqrt(dx * dx + dy * dy);
                if (d > 5) {
                    this._flag = 2;
                }
            }
        }.bind(this));

        $(canvas).mouseup(function(event) {
            if (this._flag === 1) {
                if (this._id && this._type) {
                    this.meshCreate(this._type, this._id);
                    this._type = null;
                    this._id = null;
                } else if (this._mesh) {
                    this.meshMove(this._mesh);
                    this.meshDeselect();
                } else {
                    var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
                    if (pickInfo.hit && pickInfo.pickedMesh) {
                        this.meshSelect(pickInfo.pickedMesh);
                    }
                }
            } else if (this._flag === 2) {

            }
            this._flag = 0;
        }.bind(this));

        $(canvas).mousewheel(function(event) {
            event.preventDefault();
            if (this._mesh) {
                this.meshSpin(this._mesh, event.deltaY);
            } else {

            }
        }.bind(this));
    };

    CRUXER.Engine.prototype.loadScene = function() {

        // TODO : Refactor scene loading. Allow customization.

        var ground = B.Mesh.CreateGround(name, 500.0, 500.0, 1, this.scene);
        ground.material = new B.StandardMaterial("", this.scene);
        ground.material.diffuseColor.r = 0.9;
        ground.material.diffuseColor.g = 0.9;
        ground.material.diffuseColor.b = 0.9;
        ground.material.specularColor.r = 0;
        ground.material.specularColor.g = 0;
        ground.material.specularColor.b = 0;
        ground.receiveShadows = true;

        // Ground needs to participate in collision detection, but should not be exported
        ground.cruxer = {
            id: "",
            type: "floors",
            doExport: false,
            exportId: this._exportId
        };

        this._exportId++;

        var light0 = new B.HemisphericLight("", new B.Vector3(0, 1, 0), this.scene);
        light0.intensity = 0.5;


        var light = new B.DirectionalLight("", new B.Vector3(-100, -20, -100), this.scene);
        light.position = new B.Vector3(100, 20, 100);
        light.intensity = 0.5;

        this.shadowGenerator = new BABYLON.ShadowGenerator(4096, light);
        this.shadowGenerator.useVarianceShadowMap = false;
        this.shadowGenerator.usePoissonSampling = true;
    };

    CRUXER.Engine.prototype.meshSelect = function(mesh) {
        if (mesh.cruxer.type !== "holds" && mesh.cruxer.type !== "walls") {
            return;
        }

        this._mesh = mesh;
        this._mesh.isPickable = false;
        this._mesh.enableEdgesRendering();

        this._mesh.material.alpha = 0.2;
        this._mesh.edgesColor.r = 1;
        this._mesh.edgesColor.g = 0;
        this._mesh.edgesColor.b = 0;
        this._mesh.edgesColor.a = 1;
        this._mesh.edgesWidth = 10;
    };

    CRUXER.Engine.prototype.meshDeselect = function() {
        if (!this._mesh) {
            return;
        }

        this._mesh.material.alpha = 1;
        this._mesh.edgesColor.r = 0;
        this._mesh.edgesColor.g = 0;
        this._mesh.edgesColor.b = 0;
        this._mesh.edgesColor.a = 0;
        this._mesh.edgesWidth = 0;

        this._mesh.disableEdgesRendering();
        this._mesh.isPickable = true;
        this._mesh = null;
    };

    CRUXER.Engine.prototype.meshCreate = function(type, id) {

        var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

        if (!pickInfo.hit || !collides(type, pickInfo.pickedMesh.cruxer.type)) {
            return;
        }

        var placeMesh = function(task) {
            var mesh = task.loadedMeshes[0];
            mesh.material = new B.StandardMaterial("", this.scene);
            mesh.material.specularColor.r = 0;
            mesh.material.specularColor.g = 0;
            mesh.material.specularColor.b = 0;
            mesh.receiveShadows = true;

            this.shadowGenerator.getShadowMap().renderList.push(mesh);

            // Object for all cruxer-related variables
            mesh.cruxer = {
                id: id,
                type: type,
                doExport: true,
                exportId: this._exportId
            };
            this._exportId++;

            placeOn(mesh, pickInfo);
        }.bind(this);

        var loadMesh = function(response) {
            var task = this.assets.addMeshTask("", "", urlRoot(response.model), urlFile(response.model));
            task.onSuccess = placeMesh;
            this.assets.load();
        }.bind(this);

        $.ajax({
            type: "get",
            url: type + "/" + id,
            context: this,
            success: loadMesh
        });
    };

    CRUXER.Engine.prototype.meshMove = function(mesh) {

        var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

        if (!pickInfo.hit || !collides(mesh.cruxer.type, pickInfo.pickedMesh.cruxer.type)) {
            return;
        }

        placeOn(mesh, pickInfo);
    };

    CRUXER.Engine.prototype.meshSpin = function(mesh, dir) {

        var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

        if (!pickInfo.hit || !collides(mesh.cruxer.type, pickInfo.pickedMesh.cruxer.type)) {
            return;
        }

        mesh.cruxer.spin += (dir * 0.05);

        placeOn(mesh, pickInfo);
    };

    CRUXER.Engine.prototype.meshDelete = function(mesh) {

        var inUse = this.scene.meshes.some(function(m) {
            return m.cruxer.surface === mesh;
        });

        if (inUse) {
            console.log("In Use.");
            return;
        }

        mesh.material.dispose();

        mesh.dispose();
    };

    CRUXER.Engine.prototype.engineResize = function() {

        this.engine.resize();
    };

    CRUXER.Engine.prototype.engineDebug = function() {
        if (this.scene.debugLayer.isVisible()) {
            this.scene.debugLayer.hide();
        } else {
            this.scene.debugLayer.show();
        }
    };

    CRUXER.Engine.prototype.engineExport = function() {

        var meshMap = {};
        // First Pass : Generate map of all data to export (mapped by their exportId)
        this.scene.meshes.forEach(function(m) {
            if (!m.cruxer.doExport) {
                return;

            } else if (m.cruxer.type === "walls") {
                meshMap[m.cruxer.exportId] = {
                    id: m.cruxer.id,
                    type: m.cruxer.type,
                    position: m.cruxer.position,
                    spin: m.cruxer.spin,
                    rotAxis: m.cruxer.rotAxis,
                    rotAngle: m.cruxer.rotAngle,
                    children: []
                };

            } else if (m.cruxer.type === "holds") {
                meshMap[m.cruxer.exportId] = {
                    id: m.cruxer.id,
                    type: m.cruxer.type,
                    position: m.cruxer.position,
                    spin: m.cruxer.spin,
                    rotAxis: m.cruxer.rotAxis,
                    rotAngle: m.cruxer.rotAngle
                };
            }
        });

        var meshTree = [];
        // Second Pass : Build the data tree to export
        this.scene.meshes.forEach(function(m) {
            if (!m.cruxer.doExport) {
                return;

            } else if (m.cruxer.type === "walls") {
                meshTree.push(meshMap[m.cruxer.exportId]);

            } else if (m.cruxer.type === "holds") {
                meshMap[m.cruxer.surface.cruxer.exportId].children.push(meshMap[m.cruxer.exportId]);
            }
        });

        return meshTree;
    };

    CRUXER.Engine.prototype.run = function() {

        this.engine.runRenderLoop(function() {
            this.scene.render();
        }.bind(this));
    };

    return CRUXER;

}(CRUXER || {}, $, BABYLON));
