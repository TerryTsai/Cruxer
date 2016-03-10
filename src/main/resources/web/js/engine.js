/**
 * Created by Terry on 3/6/2016.
 */

var CRUXER = (function (C, B) {

    "use strict";

    C.KEYCODES = {
        /* Common Key Code Constants */
        DOM_VK_CANCEL: 3,
        DOM_VK_HELP: 6,
        DOM_VK_BACK_SPACE: 8,
        DOM_VK_TAB: 9,
        DOM_VK_CLEAR: 12,
        DOM_VK_RETURN: 13,
        DOM_VK_ENTER: 14,
        DOM_VK_SHIFT: 16,
        DOM_VK_CONTROL: 17,
        DOM_VK_ALT: 18,
        DOM_VK_PAUSE: 19,
        DOM_VK_CAPS_LOCK: 20,
        DOM_VK_ESCAPE: 27,
        DOM_VK_SPACE: 32,
        DOM_VK_PAGE_UP: 33,
        DOM_VK_PAGE_DOWN: 34,
        DOM_VK_END: 35,
        DOM_VK_HOME: 36,
        DOM_VK_LEFT: 37,
        DOM_VK_UP: 38,
        DOM_VK_RIGHT: 39,
        DOM_VK_DOWN: 40,
        DOM_VK_PRINTSCREEN: 44,
        DOM_VK_INSERT: 45,
        DOM_VK_DELETE: 46,
        DOM_VK_0: 48,
        DOM_VK_1: 49,
        DOM_VK_2: 50,
        DOM_VK_3: 51,
        DOM_VK_4: 52,
        DOM_VK_5: 53,
        DOM_VK_6: 54,
        DOM_VK_7: 55,
        DOM_VK_8: 56,
        DOM_VK_9: 57,
        DOM_VK_SEMICOLON: 59,
        DOM_VK_EQUALS: 61,
        DOM_VK_A: 65,
        DOM_VK_B: 66,
        DOM_VK_C: 67,
        DOM_VK_D: 68,
        DOM_VK_E: 69,
        DOM_VK_F: 70,
        DOM_VK_G: 71,
        DOM_VK_H: 72,
        DOM_VK_I: 73,
        DOM_VK_J: 74,
        DOM_VK_K: 75,
        DOM_VK_L: 76,
        DOM_VK_M: 77,
        DOM_VK_N: 78,
        DOM_VK_O: 79,
        DOM_VK_P: 80,
        DOM_VK_Q: 81,
        DOM_VK_R: 82,
        DOM_VK_S: 83,
        DOM_VK_T: 84,
        DOM_VK_U: 85,
        DOM_VK_V: 86,
        DOM_VK_W: 87,
        DOM_VK_X: 88,
        DOM_VK_Y: 89,
        DOM_VK_Z: 90,
        DOM_VK_CONTEXT_MENU: 93,
        DOM_VK_NUMPAD0: 96,
        DOM_VK_NUMPAD1: 97,
        DOM_VK_NUMPAD2: 98,
        DOM_VK_NUMPAD3: 99,
        DOM_VK_NUMPAD4: 100,
        DOM_VK_NUMPAD5: 101,
        DOM_VK_NUMPAD6: 102,
        DOM_VK_NUMPAD7: 103,
        DOM_VK_NUMPAD8: 104,
        DOM_VK_NUMPAD9: 105,
        DOM_VK_MULTIPLY: 106,
        DOM_VK_ADD: 107,
        DOM_VK_SEPARATOR: 108,
        DOM_VK_SUBTRACT: 109,
        DOM_VK_DECIMAL: 110,
        DOM_VK_DIVIDE: 111,
        DOM_VK_F1: 112,
        DOM_VK_F2: 113,
        DOM_VK_F3: 114,
        DOM_VK_F4: 115,
        DOM_VK_F5: 116,
        DOM_VK_F6: 117,
        DOM_VK_F7: 118,
        DOM_VK_F8: 119,
        DOM_VK_F9: 120,
        DOM_VK_F10: 121,
        DOM_VK_F11: 122,
        DOM_VK_F12: 123,
        DOM_VK_F13: 124,
        DOM_VK_F14: 125,
        DOM_VK_F15: 126,
        DOM_VK_F16: 127,
        DOM_VK_F17: 128,
        DOM_VK_F18: 129,
        DOM_VK_F19: 130,
        DOM_VK_F20: 131,
        DOM_VK_F21: 132,
        DOM_VK_F22: 133,
        DOM_VK_F23: 134,
        DOM_VK_F24: 135,
        DOM_VK_NUM_LOCK: 144,
        DOM_VK_SCROLL_LOCK: 145,
        DOM_VK_COMMA: 188,
        DOM_VK_PERIOD: 190,
        DOM_VK_SLASH: 191,
        DOM_VK_BACK_QUOTE: 192,
        DOM_VK_OPEN_BRACKET: 219,
        DOM_VK_BACK_SLASH: 220,
        DOM_VK_CLOSE_BRACKET: 221,
        DOM_VK_QUOTE: 222,
        DOM_VK_META: 224
    };

    C.getFilenameFromUrl = function (url) {
        //URL Tools - http://befused.com/javascript/get-filename-url

        //this removes the anchor at the end, if there is one
        url = url.substring(0, (url.indexOf("#") == -1) ? url.length : url.indexOf("#"));

        //this removes the query after the file name, if there is one
        url = url.substring(0, (url.indexOf("?") == -1) ? url.length : url.indexOf("?"));

        //this removes everything before the last slash in the path
        return url.substring(url.lastIndexOf("/") + 1, url.length);
    };

    C.getRootUrlFromUrl = function (url) {
        //URL Tools - http://befused.com/javascript/get-filename-url
        return url.substring(0, url.lastIndexOf("/") + 1);
    };

    C.generateGround = function (scene, name, type) {
        var ground = B.Mesh.CreateGround(name, 200.0, 200.0, 1, scene),
            material = new B.StandardMaterial(name + ".tex", scene);

        if (type === 0) {
            // Ground Material
            material.reflectionTexture = new B.CubeTexture("/tex/TropicalSunnyDay", scene);
            material.diffuseColor = new B.Color3(0, 0, 0);
            material.emissiveColor = new B.Color3(0.5, 0.5, 0.5);
            material.alpha = 0.2;
            material.specularPower = 16;

            // Fresnel
            material.reflectionFresnelParameters = new B.FresnelParameters();
            material.reflectionFresnelParameters.bias = 0.1;

            material.emissiveFresnelParameters = new B.FresnelParameters();
            material.emissiveFresnelParameters.bias = 0.6;
            material.emissiveFresnelParameters.power = 4;
            material.emissiveFresnelParameters.leftColor = B.Color3.White();
            material.emissiveFresnelParameters.rightColor = B.Color3.Black();

            material.opacityFresnelParameters = new B.FresnelParameters();
            material.opacityFresnelParameters.leftColor = B.Color3.White();
            material.opacityFresnelParameters.rightColor = B.Color3.Black();
        } else if (type === 1) {
            material.diffuseTexture = new B.Texture("/tex/floor.jpg", scene);
            material.diffuseTexture.uScale = 1.0;
            material.diffuseTexture.vScale = 1.0;
            material.backFaceCulling = false;
        } else if (type === 2) {
            material.diffuseTexture = new B.Texture("/tex/floor.jpg", scene);
            material.diffuseTexture.uScale = 6;
            material.diffuseTexture.vScale = 6;
            material.specularColor = new B.Color3(0, 0, 0);
        } else {
            material.diffuseColor = new B.Color3(0.9, 0.9, 0.9);
        }

        ground.material = material;
        return ground;
    };

    C.generateLight = function (scene, name) {
        var light = new B.HemisphericLight(name, new B.Vector3(0, 1, 0), scene);
        light.intensity = 0.9;
        return light;
    };

    C.generateSkybox = function (scene, name, file, type) {
        var skybox = B.Mesh.CreateBox(name, 1000.0, scene),
            skyboxMaterial = new B.StandardMaterial("skyBox", scene);

        if (type === 0) {
            skyboxMaterial.reflectionTexture = new B.CubeTexture("/tex/" + file, scene);
            skyboxMaterial.reflectionTexture.coordinatesMode = B.Texture.SKYBOX_MODE;
        }

        skyboxMaterial.backFaceCulling = false;
        skyboxMaterial.diffuseColor = new B.Color3(0, 0, 0);
        skyboxMaterial.specularColor = new B.Color3(0, 0, 0);
        skyboxMaterial.disableLighting = true;
        skybox.material = skyboxMaterial;

        return skybox;
    };

    return C;

}(CRUXER || {}, BABYLON));

// TODO : Remove dependency on document and $
var CRUXER = (function (C, B, D, J) {

    "use strict";

    // id - xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    // type - holds, walls, floors, decor
    // usage - template, instance

    function initDOM(engine) {
        engine.canvas = J("#cruxer-canvas")[0];
        engine.canvas.addEventListener("click", function () {
            if (this.selectionId !== null && this.selectionType !== null) {
                this.placeInstance(this.selectionId, this.selectionType);
                this.selectionId = null;
                this.selectionType = null;
            }
        }.bind(engine));

        J(".cruxer-submit").click(function () {
            engine.exportRoute();
        });

        J(".cruxer-hold").click(function () {
            var id = J(this).data("id");
            engine.selectionId = id;
            engine.selectionType = "holds";
        });

        J(".cruxer-wall").click(function () {
            var id = J(this).data("id");
            engine.selectionId = id;
            engine.selectionType = "walls";
        });
    }

    function initWindow(engine, window) {

        // Engine Resizing
        window.addEventListener("resize", function () {
            this.engine.resize();
        }.bind(engine));

        // Debug Toggling
        window.addEventListener("keydown", function (event) {
            switch (event.keyCode) {
                case C.KEYCODES.DOM_VK_TAB:
                    var debug = this.scene.debugLayer;
                    if (debug.isVisible()) {
                        debug.hide();
                    } else {
                        debug.show();
                    }
                    break;
                case C.KEYCODES.DOM_VK_2:
                    var mesh = this.scene.getMeshByName("hold").createInstance("hold" + j);
                    mesh.setAbsolutePosition(new B.Vector3(Math.random() * 200 - 100, 0, Math.random() * 200 - 100));
                    j = j + 1;
                    break;
                case C.KEYCODES.COM_VK_ADD:
                    if (engine.selectionInstance !== null) {
                        rotateOn(engine.selectionInstance, 0.1);
                    }
                    break;
                case C.KEYCODES.COM_VK_SUBTRACT:
                    if (engine.selectionInstance !== null) {
                        rotateOn(engine.selectionInstance, -0.1);
                    }
                    break;
            }
        }.bind(engine));

    }

    function collidesWith(type, mesh) {
        if (mesh.usage === "template") {
            return false;
        } else if (type === "holds" && mesh.type === "walls") {
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

    function rotateOn(instance, degrees) {
        instance.rotate(B.Axis.Y, degrees, B.Space.LOCAL);
    }

    function instanceAt(id, type, mesh, posOrient) {
        var instance = mesh.createInstance("");
        instance.id = id;
        instance.type = type;
        instance.usage = "instance";
        instance.position = new B.Vector3(posOrient.px, posOrient.py, posOrient.pz);
        instance.rotationQuaternion = new B.Quaternion(posOrient.qx, posOrient.qy, posOrient.qz, posOrient.qw);
    }

    function instanceOn(id, type, mesh, pickInfo) {
        var instance = mesh.createInstance(""),
            position = pickInfo.pickedPoint,
            normal = pickInfo.getNormal(),
            rotAxis = B.Vector3.Cross(B.Axis.Y, normal).normalize(),
            rotAngle = Math.acos(B.Vector3.Dot(normal, B.Axis.Y));

        instance.id = id;
        instance.type = type;
        instance.usage = "instance";
        instance.rotate(rotAxis, rotAngle);
        instance.position = position;
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
    }

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
    }

    function addMeshTask(modelUrl, asset, success) {
        var task = asset.addMeshTask("", "",
            C.getRootUrlFromUrl(modelUrl),
            C.getFilenameFromUrl(modelUrl)
        );
        task.onSuccess = success;
        return task;
    }

    function loadTypeTpl(response, type, asset, callback) {
        addMeshTask(response.model, asset, function (task) {
            task.loadedMeshes.forEach(function (mesh) {
                mesh.id = response.id;
                mesh.type = type;
                mesh.usage = "template";
                mesh.isVisible = false;
                mesh.isPickable = false;
            });
        });

        asset.onFinish = function (tasks) {
            if (tasks !== null) asset.reset();
            if (callback !== undefined && callback !== null) callback();
        };

        asset.load();
    }

    function loadRouteTpl(response, asset, callback) {
        var wallSeen = {};
        response.wallInstances
            .map(function (inst) {
                return inst.wall;
            })
            .forEach(function (wall) {
                if (!wallSeen.hasOwnProperty(wall.id)) {
                    wallSeen[wall.id] = true;
                    addMeshTask(wall.model, asset, function (task) {
                        task.loadedMeshes.forEach(function (mesh) {
                            mesh.id = wall.id;
                            mesh.type = "walls";
                            mesh.usage = "template";
                            mesh.isVisible = false;
                            mesh.isPickable = false;
                        });
                    });

                }
            });

        var holdSeen = {};
        response.holdInstances
            .map(function (inst) {
                return inst.hold;
            })
            .forEach(function (hold) {
                if (!holdSeen.hasOwnProperty(hold.id)) {
                    holdSeen[hold.id] = true;
                    addMeshTask(hold.model, asset, function (task) {
                        task.loadedMeshes.forEach(function (mesh) {
                            mesh.id = hold.id;
                            mesh.type = "holds";
                            mesh.usage = "template";
                            mesh.isVisible = false;
                            mesh.isPickable = false;
                        });
                    });
                }
            });

        asset.onFinish = function (tasks) {
            if (tasks !== null) asset.reset();
            if (callback !== undefined && callback !== null) callback();
        };

        asset.load();
    }

    function getMetaCsrf() {
        return {
            token: J("meta[name='csrf-token']").attr("content"),
            header: J("meta[name='csrf-header']").attr("content")
        };
    }

    C.Engine = function (window) {

        initDOM(this);

        initWindow(this, window);

        // Babylon Engine
        // enableOfflineSupport - http://www.html5gamedevs.com/topic/13548-is-it-possible-to-stop-the-manifest-error/
        this.engine = new B.Engine(this.canvas, true);
        this.engine.enableOfflineSupport = false;

        // Babylon Scene
        this.scene = new B.Scene(this.engine);
        this.scene.clearColor = B.Color3.FromHexString("#FFFFFF");

        // Babylon Camera
        //this.camera = new B.CustomJoysticksCamera("camera", new B.Vector3(0, 25, 100), this.scene);
        //this.camera = new B.VirtualJoysticksCamera("camera", new B.Vector3(0, 25, 100), this.scene);
        //this.camera = new B.DeviceOrientationCamera("camera", new B.Vector3(0, 25, 100), this.scene);
        //this.camera = new B.TouchCamera("camera",  new B.Vector3(0, 25, 100), this.scene);
        this.camera = new B.FreeCamera("camera", new B.Vector3(0, 25, 100), this.scene);

        this.scene.activeCamera = this.camera;
        this.camera.setTarget(B.Vector3.Zero());
        this.camera.attachControl(this.canvas, false);

        // Babylon Assets
        // defaultLoadingScreen does not return control to camera after loading
        // assets tasks need to be reset to avoid being rerun
        this.assets = new B.AssetsManager(this.scene);
        this.assets.useDefaultLoadingScreen = false;
        this.assets.onFinish = function (tasks) {
            console.log("AssetManager completed " + tasks.length + " tasks.");
            if (tasks !== null) {
                this.assets.reset();
            }
        }.bind(this);

        // Cruxer Context
        this.selectionId = null;
        this.selectionType = null;
        this.selectionInstance = null;
    };

    C.Engine.prototype.loadScene = function () {
        var floor = C.generateGround(this.scene, "", 3);
        floor.type = "floors";
        floor.usage = "instance";

        var light0 = new B.HemisphericLight("", new B.Vector3(0, 1, 0), this.scene);
        light0.intensity = 0.9;

        // var light1 = new B.PointLight("", new B.Vector3(0, 0, 0), this.scene);
        // light1.diffuse = new B.Color3(1, 0, 0);
        // light1.specular = new B.Color3(1, 1, 1);

        this.assets.load();
    };

    C.Engine.prototype.placeInstance = function (id, type, orient) {

        var template = findOneMesh(id, type, "template", this.scene);

        if (template === null) {

            // If template not found, load and try again.
            J.get(type + "/" + id, function (response) {
                loadTypeTpl(response, type, this.assets, function() {

                    // If template is found, place. Otherwise ignore?
                    var template = findOneMesh(id, type, "template", this.scene);
                    if (template !== null)
                        this.placeInstance(id, type, orient)

                }.bind(this));
            }.bind(this));

        } else if (orient !== undefined && orient !== null) {

            // Use orient to position mesh if provided
            instanceAt(id, type, template, orient);

        } else {

            // Otherwise use pointer pickInfo
            var pickInfo = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

            if (pickInfo.hit && collidesWith(type, pickInfo.pickedMesh)) {
                instanceOn(id, type, template, pickInfo);
            }
        }
    };

    C.Engine.prototype.importRoute = function (id) {

        J.get("/routes/" + id, function (route) {

            loadRouteTpl(route, this.assets, function () {
                D.getElementById("cruxer-route-name").value = route.name;

                route.wallInstances.forEach(function (wi) {
                    this.placeInstance(wi.wall.id, "walls", wi);
                }.bind(this));

                route.holdInstances.forEach(function (hi) {
                    this.placeInstance(hi.hold.id, "holds", hi);
                }.bind(this));
            }.bind(this));

        }.bind(this));
    };

    C.Engine.prototype.exportRoute = function () {

        var csrf = getMetaCsrf(),
            headers = {},
            thumbnail = null,
            wallInstances = [],
            holdInstances = [],
            name = D.getElementById("cruxer-route-name").value;

        B.Tools.CreateScreenshot(
            this.engine,
            this.camera,
            {width: 400, height: 200},
            function (img) {
                thumbnail = img;
            }
        );

        findAllMesh(null, "walls", "instance", this.scene)
            .forEach(function (instance) {
                wallInstances.push({
                    wall: {id: instance.id},
                    px: instance.position.x,
                    py: instance.position.y,
                    pz: instance.position.z,
                    qw: instance.rotationQuaternion.w,
                    qx: instance.rotationQuaternion.x,
                    qy: instance.rotationQuaternion.y,
                    qz: instance.rotationQuaternion.z
                });
            });

        findAllMesh(null, "holds", "instance", this.scene)
            .forEach(function (instance) {
                holdInstances.push({
                    hold: {id: instance.id},
                    px: instance.position.x,
                    py: instance.position.y,
                    pz: instance.position.z,
                    qw: instance.rotationQuaternion.w,
                    qx: instance.rotationQuaternion.x,
                    qy: instance.rotationQuaternion.y,
                    qz: instance.rotationQuaternion.z
                });
            });

        headers[csrf.header] = csrf.token;

        console.log(wallInstances);
        console.log(holdInstances);

        J.ajax({
            method: "POST",
            url: "/routes",
            headers: headers,
            data: {
                name : name,
                thumbnail : thumbnail,
                walls : JSON.stringify(wallInstances),
                holds : JSON.stringify(holdInstances)
            },
            success: function () {}
        });
    };

    C.Engine.prototype.select = function (id, type) {
        this.selectionId = id;
        this.selectionType = type;
    };

    C.Engine.prototype.run = function () {
        this.engine.runRenderLoop(function () {
            this.scene.render();
        }.bind(this));
    };

    return C;

}(CRUXER || {}, BABYLON, document, $));