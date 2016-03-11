/**
 * Cruxer Engine
 */

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

    function rotateOn(instance, degrees) {
        instance.rotate(B.Axis.Y, degrees, B.Space.LOCAL);
    };

    function moveOn(instance, pickInfo) {
        var position = pickInfo.pickedPoint,
            normal = pickInfo.getNormal(),
            rotAxis = B.Vector3.Cross(B.Axis.Y, normal).normalize(),
            rotAngle = Math.acos(B.Vector3.Dot(normal, B.Axis.Y));

        instance.rotate(rotAxis, rotAngle);
        instance.position = position;
    };

    function moveTo(instance, posOrient) {
        instance.position = new B.Vector3(posOrient.px, posOrient.py, posOrient.pz);
        instance.rotationQuaternion = new B.Quaternion(posOrient.qx, posOrient.qy, posOrient.qz, posOrient.qw);
    };

    function collidesWith(type, mesh) {
        if (type === "holds" && mesh.type === "walls") {
            return true;
        } else if (type === "walls" && mesh.type === "floors") {
            return true;
        } else if (type === "decor" && mesh.type === "floors") {
            return true;
        } else if (type === "decor" && mesh.type === "walls") {
            return true;
        } else {
            return false
        }
    }

    function findOneMesh(id, type, usage, scene) {
        for (var i = 0; i < scene.meshes.length; i++) {
            if (id !== null && scene.meshes[i].id !== id) {
                // continue;
            } else if (type !== null && scene.meshes[i].type !== type) {
                // continue;
            } else if (usage !== null && scene.meshes[i].usage !== usage) {
                // continue;
            } else {
                return scene.meshes[i];
            }
        }
        return null;
    };

    function findAllMesh(id, type, usage, scene) {
        return scene.meshes.filter(function (mesh) {
            if (id !== null && mesh.id !== id) {
                return false;
            } else if (type !== null && mesh.type !== type) {
                return false;
            } else if (usage !== null && mesh.usage !== usage) {
                return false;
            } else {
                return true;
            }
        });
    };

    function urlFilename(url) {
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

    CRUXER.Engine = function(window, canvas) {
        // Babylon Engine
        // enableOfflineSupport - http://www.html5gamedevs.com/topic/13548-is-it-possible-to-stop-the-manifest-error/
        this.engine = new B.Engine(canvas, true);
        this.engine.enableOfflineSupport = false;

        // Babylon Scene
        this.scene = new B.Scene(this.engine);
        this.scene.clearColor = B.Color3.FromHexString("#FFFFFF");

        // Babylon Camera
        this.camera = new B.FreeCamera("camera", new B.Vector3(0, 25, 100), this.scene);
        this.camera.setTarget(B.Vector3.Zero());
        this.camera.attachControl(canvas, false);
        this.scene.activeCamera = this.camera;

        // Babylon Assets
        // defaultLoadingScreen does not return control to camera after loading
        // assets tasks need to be reset to avoid being rerun
        this.assets = new B.AssetsManager(this.scene);
        this.assets.useDefaultLoadingScreen = false;
        this.assets.onFinish = function (tasks) {
            if (tasks !== null) { this.reset(); }
        };

        // Window Bindings
        window.addEventListener("resize", function () {
            this.engine.resize();
        }.bind(this));

        // Canvas Bindings
        canvas.addEventListener("click", function () {
            this.placeObject("walls", "e3374a08-2fc0-47f1-aa83-8ade7e4259a1");
        }.bind(this));
    };

    CRUXER.Engine.prototype.loadScene = function () {

        var groundMat = new B.StandardMaterial("", this.scene);
        groundMat.diffuseColor = new B.Color3(0.9, 0.9, 0.9);

        var ground = B.Mesh.CreateGround(name, 200.0, 200.0, 1, this.scene);
        ground.material = groundMat;
        ground.type = "floors";

        var light0 = new B.HemisphericLight("", new B.Vector3(0, 1, 0), this.scene);
        light0.intensity = 0.9;

        var light1 = new B.PointLight("", new B.Vector3(0, 0, 0), this.scene);
        light1.diffuse = new B.Color3(1, 0, 0);
        light1.specular = new B.Color3(1, 1, 1);

    };

    CRUXER.Engine.prototype.placeObject = function (type, id) {

        var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

        if (pickInfo.hit && collidesWith(type, pickInfo.pickedMesh)) {
            var placeMesh = function(task) {
                var mesh = task.loadedMeshes[0];
                mesh.id = id;
                mesh.type = type;
                moveOn(mesh, pickInfo);
            };

            var loadMesh = function(response) {
                var task = this.assets.addMeshTask("", "", urlRoot(response.model), urlFilename(response.model));
                task.onSuccess = placeMesh;
                this.assets.load();
            }.bind(this);

            $.ajax({
                type: "get",
                url: type + "/" + id,
                context: this,
                success: loadMesh
            });
        }
    };

    CRUXER.Engine.prototype.run = function () {
        this.engine.runRenderLoop(function () {
            this.scene.render();
        }.bind(this));
    };

    return CRUXER;

}(CRUXER || {}, $, BABYLON));
