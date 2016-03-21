/**
 * Created by Terry on 3/10/2016.
 */

if (!window.File || !window.FileReader) {
    console.log('The File APIs are not fully supported in this browser. Thumbnail module may not work.');
}

var v;

/**
 * Attaches a file input to a ThreeJS canvas allowing
 * .obj models to be previewed or used for thumbnails
 * by calling toDataURL() on the canvas.
 *
 */
var THUMB = (function(THUMB, JSM) {

    "use strict";

    THUMB.attach = function(canvas, input, onReady) {

        onReady = onReady || function() {};

        var viewerSettings = {
            cameraEyePosition: [0.0, 5.0 , 10.0],
            cameraCenterPosition: [0.0, 0.0, 0.0],
            cameraUpVector: [0.0, 1.0, 0.0]
        };

        var viewerInitialize = function(e) {
            // TODO : Should not be remaking this each time we load a file.
            var viewer = new JSM.ThreeViewer();
            var json = JSM.ConvertObjToJsonData(e.target.result);
            var meshes = JSM.ConvertJSONDataToThreeMeshes(json);
            viewer.Start(canvas, viewerSettings);
            viewer.RemoveMeshes();
            viewer.AddMeshes(meshes);
            viewer.Draw();
            onReady();
            v = viewer;
        };

        input.onchange = function(e) {
            var file = e.target.files[0];
            var reader = new FileReader();
            reader.onload = viewerInitialize;
            reader.readAsText(file);
            onReady();
        };
    };

    return THUMB;

}(THUMB || {}, JSM));