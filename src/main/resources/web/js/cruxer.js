/* Cruxer Engine */

var CRUXER = (function(CRUXER, $, B) {

    "use strict";

    function getMetaCsrf() {
        var tokenElement = document.querySelector('meta[name="_csrf"]');
        var headerElement = document.querySelector('meta[name="_csrf_header"]');

        return {
            token: tokenElement && tokenElement.getAttribute("content"),
            header: headerElement && headerElement.getAttribute("content")
        };
    }

    function urlFile(url) {
        //URL Tools - http://befused.com/javascript/get-filename-url

        //this removes the anchor at the end, if there is one
        url = url.substring(0, (url.indexOf("#") == -1) ? url.length : url.indexOf("#"));

        //this removes the query after the file name, if there is one
        url = url.substring(0, (url.indexOf("?") == -1) ? url.length : url.indexOf("?"));

        //this removes everything before the last slash in the path
        return url.substring(url.lastIndexOf("/") + 1, url.length);
    }

    function urlRoot(url) {
        //URL Tools - http://befused.com/javascript/get-filename-url
        return url.substring(0, url.lastIndexOf("/") + 1);
    }

    function placeOn(mesh, pickInfo) {
        var normal = pickInfo.getNormal();

        // Get Local Coordinates
        var matrix = new BABYLON.Matrix();
        pickInfo.pickedMesh.getWorldMatrix().invertToRef(matrix);
        var local = BABYLON.Vector3.TransformCoordinates(pickInfo.pickedPoint, matrix);

        // Update Cruxer Variables
        mesh.cruxer.position = local;
        mesh.cruxer.spin = mesh.cruxer.spin || 0;
        mesh.cruxer.rotAxis = B.Vector3.Cross(B.Axis.Y, normal).normalize();
        mesh.cruxer.rotAngle = Math.acos(B.Vector3.Dot(normal, B.Axis.Y));
        mesh.cruxer.surface = pickInfo.pickedMesh;
    }

    function updateMesh(mesh) {

        // Reset Rotation
        if (mesh.rotationQuaternion) {
            mesh.rotationQuaternion.x = 0;
            mesh.rotationQuaternion.y = 0;
            mesh.rotationQuaternion.z = 0;
            mesh.rotationQuaternion.w = 1;
        }

        // Update Pose
        mesh.parent = mesh.cruxer.surface;
        mesh.position = mesh.cruxer.position;
        mesh.rotate(mesh.cruxer.rotAxis, mesh.cruxer.rotAngle);
        mesh.rotate(B.Axis.Y, mesh.cruxer.spin, B.Space.LOCAL);

        // Update Material
        mesh.material.diffuseColor.r = mesh.cruxer.diffuse.r;
        mesh.material.diffuseColor.g = mesh.cruxer.diffuse.g;
        mesh.material.diffuseColor.b = mesh.cruxer.diffuse.b;
    }

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
    }

    function toDTO(mesh) {
        if (!mesh.cruxer || !mesh.cruxer.doExport) {
            return null;

        } else if (mesh.cruxer.type === "walls") {
            return {
                holdInstances: [],
                wall: { id: mesh.cruxer.id },
                pose: {
                    px: mesh.cruxer.position.x,
                    py: mesh.cruxer.position.y,
                    pz: mesh.cruxer.position.z,
                    rx: mesh.cruxer.rotAxis.x,
                    ry: mesh.cruxer.rotAxis.y,
                    rz: mesh.cruxer.rotAxis.z,
                    rotAngle: mesh.cruxer.rotAngle,
                    spin: mesh.cruxer.spin
                },
                material: {
                    dr: mesh.cruxer.diffuse.r,
                    dg: mesh.cruxer.diffuse.g,
                    db: mesh.cruxer.diffuse.b
                }
            };

        } else if (mesh.cruxer.type === "holds") {
            return {
                hold: { id: mesh.cruxer.id },
                pose: {
                    px: mesh.cruxer.position.x,
                    py: mesh.cruxer.position.y,
                    pz: mesh.cruxer.position.z,
                    rx: mesh.cruxer.rotAxis.x,
                    ry: mesh.cruxer.rotAxis.y,
                    rz: mesh.cruxer.rotAxis.z,
                    rotAngle: mesh.cruxer.rotAngle,
                    spin: mesh.cruxer.spin
                },
                material: {
                    dr: mesh.cruxer.diffuse.r,
                    dg: mesh.cruxer.diffuse.g,
                    db: mesh.cruxer.diffuse.b
                }
            };

        } else {
            return null;
        }
    }

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
            if (tasks) this.reset();
        };

        // State Variables
        this._id = null;
        this._type = null;
        this._mesh = null;
        this._flag = 0;
        this._prevX = 0;
        this._prevY = 0;
        this._mode = 0;
        this._color = "#888888";

        // Canvas Bindings
        canvas.addEventListener('ontouchstart', function(e) { e.preventDefault() });
        canvas.addEventListener('ontouchmove', function(e) { e.preventDefault() });
        canvas.addEventListener('contextmenu', function(e) { e.preventDefault() });
        canvas.addEventListener('mousewheel', function(e) { e.preventDefault() });

        // TODO : Canvas bindings are a bit of a mess.
        $(canvas).mousedown(function(event) {
            if (event.which === 1) {
                this._flag = 1;
                this._prevX = event.pageX;
                this._prevY = event.pageY;
            }
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
                    if (this._mode === 0) {
                        this.meshCreate(this._type, this._id);
                        this._type = null;
                        this._id = null;
                    }
                } else if (this._mesh) {
                    this.meshMove(this._mesh);
                    this.meshDeselect();
                } else {
                    var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
                    if (this._mode === 0) {
                        if (pickInfo.hit && pickInfo.pickedMesh) {
                            this.meshSelect(pickInfo.pickedMesh);
                        }
                    } else if (this._mode === 1) {
                        if (pickInfo.hit && pickInfo.pickedMesh) {
                            this.meshDelete(pickInfo.pickedMesh);
                            this._mode = 0;
                        }
                    }
                }
            } else if (this._flag === 2) {

            }

            if (event.which === 1) {
                this._flag = 0;
            }
        }.bind(this));

        $(canvas).mousewheel(function(event) {
            event.preventDefault();
            if (this._mesh) {
                this.meshSpin(this._mesh, event.deltaY);
            } else {
                this.cameraZoom(event.deltaY);
            }
        }.bind(this));

        $(canvas).keydown(function(event) {
            event.preventDefault();
            console.log(event);
        }.bind(this));
    };

    CRUXER.Engine.prototype.loadScene = function() {

        // TODO : Refactor scene loading. Allow customization.

        this.ground = B.Mesh.CreateGround(name, 500.0, 500.0, 1, this.scene);
        this.ground.material = new B.StandardMaterial("", this.scene);
        this.ground.material.diffuseColor.r = 0.9;
        this.ground.material.diffuseColor.g = 0.9;
        this.ground.material.diffuseColor.b = 0.9;
        this.ground.material.specularColor.r = 0;
        this.ground.material.specularColor.g = 0;
        this.ground.material.specularColor.b = 0;
        this.ground.receiveShadows = true;

        // Ground needs to participate in collision detection, but should not be exported
        this.ground.cruxer = {
            id: "",
            type: "floors",
            doExport: false,
        };

        this.cursor = B.Mesh.CreateSphere('sphere1', 16, 2, this.scene);
        this.cursor.material = new B.StandardMaterial("", this.scene);
        this.cursor.material.diffuseColor.r = 0.9;
        this.cursor.material.diffuseColor.g = 0.9;
        this.cursor.material.diffuseColor.b = 0.9;
        this.cursor.isPickable = false;
        this.cursor.visibility = 0;

        var light0 = new B.HemisphericLight("", new B.Vector3(0, 1, 0), this.scene);
        light0.intensity = 0.5;


        var light = new B.DirectionalLight("", new B.Vector3(-100, -20, -100), this.scene);
        light.position = new B.Vector3(100, 20, 100);
        light.intensity = 0.5;

        this.shadowGenerator = new BABYLON.ShadowGenerator(4096, light);
        this.shadowGenerator.useVarianceShadowMap = false;
        this.shadowGenerator.usePoissonSampling = true;
    };

    CRUXER.Engine.prototype.cameraMode = function(mode) {

        this._mode = mode;
    };

    CRUXER.Engine.prototype.cameraZoom = function(dir) {
        // Adapted from https://github.com/BabylonJS/Babylon.js/blob/master/src/Cameras/Inputs/babylon.freecamera.input.keyboard.js
        var speed = this.camera._computeLocalCameraSpeed();
        this.camera._localDirection.copyFromFloats(0, 0, dir * speed);
        this.camera.getViewMatrix().invertToRef(this.camera._cameraTransformMatrix);
        BABYLON.Vector3.TransformNormalToRef(this.camera._localDirection, this.camera._cameraTransformMatrix, this.camera._transformedDirection);
        this.camera.cameraDirection.addInPlace(this.camera._transformedDirection);
    };

    CRUXER.Engine.prototype.colorSelect = function(hex) {
        this._color = hex;
        if (this._mesh) {
            this._mesh.cruxer.diffuse = B.Color3.FromHexString(hex);
            updateMesh(this._mesh);
        }
    }

    CRUXER.Engine.prototype.meshSelect = function(mesh) {
        if (mesh.cruxer.type !== "holds" && mesh.cruxer.type !== "walls") {
            return;
        }

        this._mesh = mesh;
        this._mesh.isPickable = false;
        this._mesh.enableEdgesRendering();

        this._mesh.material.alpha = 0.2;
        this._mesh.edgesColor.r = 0;
        this._mesh.edgesColor.g = 1;
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
                diffuse: B.Color3.FromHexString(this._color)
            };

            placeOn(mesh, pickInfo);
            updateMesh(mesh);
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
        updateMesh(mesh);
    };

    CRUXER.Engine.prototype.meshSpin = function(mesh, dir) {

        var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

        if (!pickInfo.hit || !collides(mesh.cruxer.type, pickInfo.pickedMesh.cruxer.type)) {
            return;
        }

        mesh.cruxer.spin += (dir * 0.05);

        placeOn(mesh, pickInfo);
        updateMesh(mesh);
    };

    CRUXER.Engine.prototype.meshDelete = function(mesh) {

        var inUse = this.scene.meshes.some(function(m) {
            return m.cruxer && m.cruxer.surface === mesh;
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

        var exportId = 0;

        // Prepare all exported objects
        var objMap = {};
        this.scene.meshes.forEach(function(m) {
            var dto = toDTO(m);
            if (dto) {
                m.cruxer.exportId = exportId;
                objMap[m.cruxer.exportId] = dto;
                exportId++;
            }
        });

        // Build exported route
        var route = {
            name: Math.random().toString(36).substring(2, 5),
            wallInstances: [],
            thumbnailRaw: null
        };

        this.scene.meshes.forEach(function(m) {
            if (!m.cruxer || !m.cruxer.doExport) {
                // Do nothing

            } else if (m.cruxer.type === "walls") {
                route.wallInstances.push(objMap[m.cruxer.exportId]);

            } else if (m.cruxer.type === "holds") {
                var wall = objMap[m.cruxer.surface.cruxer.exportId];
                wall.holdInstances.push(objMap[m.cruxer.exportId]);
            }
        });

        B.Tools.CreateScreenshot(
            this.engine,
            this.camera, { width: 400, height: 200 },
            function(img) { route.thumbnailRaw = img; }
        );

        $.ajax({
            url: "routes",
            type: "post",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(route),
            beforeSend: function(xhr) {
                var csrf = getMetaCsrf();
                xhr.setRequestHeader(csrf.header, csrf.token);
            },
            success: function(response) {
                console.log(response);
            },
            error: function(response) {
                console.log(response);
            }
        });
    };

    CRUXER.Engine.prototype.engineImport = function(id) {

        var ground = this.ground;

        // Mesh load order is not fixed, so surfaces can only be assigned after all meshes have loaded.
        var setSurfaces = function(response) {
            response.wallInstances.forEach(function(wallDTO) {
                wallDTO.mesh.cruxer.surface = ground;
                updateMesh(wallDTO.mesh);

                wallDTO.holdInstances.forEach(function(holdDTO) {
                    holdDTO.mesh.cruxer.surface = wallDTO.mesh;
                    updateMesh(holdDTO.mesh);
                });
            });
        }.bind(this);

        var loadMesh = function(dto, type, modelUrl) {
            var task = this.assets.addMeshTask("", "", urlRoot(modelUrl), urlFile(modelUrl));
            task.onSuccess = function(task) {
                var mesh = task.loadedMeshes[0];
                dto.mesh = mesh;
                dto.mesh.material = new B.StandardMaterial("", this.scene);
                dto.mesh.material.specularColor.r = 0;
                dto.mesh.material.specularColor.g = 0;
                dto.mesh.material.specularColor.b = 0;
                dto.mesh.receiveShadows = true;
                dto.type = type;

                this.shadowGenerator.getShadowMap().renderList.push(dto.mesh);

                dto.mesh.cruxer = {
                    id: dto.id,
                    type: dto.type,
                    position: new B.Vector3(dto.pose.px, dto.pose.py, dto.pose.pz),
                    rotAxis: new B.Vector3(dto.pose.rx, dto.pose.ry, dto.pose.rz),
                    diffuse: new B.Color3(dto.material.dr, dto.material.dg, dto.material.db),
                    rotAngle: dto.pose.rotAngle,
                    spin: dto.pose.spin
                };

                updateMesh(dto.mesh);

            }.bind(this);
        }.bind(this);

        var loadRoute = function(response) {
            response.wallInstances.forEach(function(wallDTO) {
                loadMesh(wallDTO, "walls", wallDTO.wall.model);
                wallDTO.holdInstances.forEach(function(holdDTO) {
                    loadMesh(holdDTO, "holds", holdDTO.hold.model);
                });
            });

            this.assets.onFinish = function(tasks) {
                setSurfaces(response);
                if (tasks) this.reset();
                this.onFinish = function(tasks) {
                    if (tasks) this.reset();
                };
            }
            this.assets.load();
        }.bind(this);

        $.ajax({
            type: "get",
            url: "routes/" + id,
            context: this,
            success: loadRoute
        });
    };

    CRUXER.Engine.prototype.engineRun = function() {

        this.engine.runRenderLoop(function() {
            this.scene.render();
            if (this.camera.position.y < 1) {
                this.camera.position.y = 1;
            }
        }.bind(this));
    };

    return CRUXER;

}(CRUXER || {}, $, BABYLON));

var engine = new CRUXER.Engine(document.getElementById('cruxer-canvas'));
engine.loadScene();
engine.engineRun();

window.addEventListener("resize", function() { engine.engineResize(); });
window.addEventListener("keypress", function(e) {
    switch (e.charCode) {
        case 96:
            engine.engineDebug();
            break;
    }
});

$('#cruxer-delete').on('click', function(e) { engine.cameraMode(1); });
$('#cruxer-save').on('click', function(e) { engine.engineExport(); });
$("#cruxer-color").spectrum({
    color: "#888888",
    change: function(color) {
        engine.colorSelect(color.toHexString());
    }.bind(this)
});



