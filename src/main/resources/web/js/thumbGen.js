if (window.File && window.FileReader && window.FileList && window.Blob) {
// Great success! All the File APIs are supported.
} else {
    console.log('The File APIs are not fully supported in this browser. Thumbnail module may not work.');
}


var THUMB = (function(THUMB) {

    THUMB.attach = function(canvas, input, submit) {
        var viewerSettings = {
            cameraEyePosition: [-2.0, -1.5, 1.0],
            cameraCenterPosition: [0.0, 0.0, 0.0],
            cameraUpVector: [0.0, 0.0, 1.0]
        };

        var viewerInitialize = function(e) {
            var viewer = new JSM.ThreeViewer();
            var json = JSM.ConvertObjToJsonData(e.target.result);
            var meshes = JSM.ConvertJSONDataToThreeMeshes(json);
            viewer.Start(canvas, viewerSettings);
            viewer.RemoveMeshes();
            viewer.AddMeshes(meshes);
            viewer.Draw();
            submit.disabled = false;
        };

        input.onchange = function(e) {
            var file = e.target.files[0];
            var reader = new FileReader();
            reader.onload = viewerInitialize;
            reader.readAsText(file);
        };
    };

    return THUMB;

}(THUMB || {}));