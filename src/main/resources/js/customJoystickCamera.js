var __extends = (this && this.__extends) || function (d, b) {
        for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
var BABYLON;
(function (BABYLON) {
    // We're mainly based on the logic defined into the FreeCamera code
    var CustomJoysticksCamera = (function (_super) {
        __extends(CustomJoysticksCamera, _super);
        function CustomJoysticksCamera(name, position, scene) {
            _super.call(this, name, position, scene);
            this._leftjoystick = new BABYLON.CustomVirtualJoystick(true);
            this._leftjoystick.setAxisForUpDown(BABYLON.JoystickAxis.Z);
            this._leftjoystick.setAxisForLeftRight(BABYLON.JoystickAxis.X);
            this._leftjoystick.setJoystickSensibility(0.75);
            this._rightjoystick = new BABYLON.CustomVirtualJoystick(false);
            this._rightjoystick.setAxisForUpDown(BABYLON.JoystickAxis.X);
            this._rightjoystick.setAxisForLeftRight(BABYLON.JoystickAxis.Y);
            this._rightjoystick.reverseUpDown = true;
            this._rightjoystick.setJoystickSensibility(0.035);
            this._rightjoystick.setJoystickColor("yellow");
        }
        CustomJoysticksCamera.prototype.getLeftJoystick = function () {
            return this._leftjoystick;
        };
        CustomJoysticksCamera.prototype.getRightJoystick = function () {
            return this._rightjoystick;
        };
        CustomJoysticksCamera.prototype._checkInputs = function () {
            var speed = this._computeLocalCameraSpeed() * 50;
            var cameraTransform = BABYLON.Matrix.RotationYawPitchRoll(this.rotation.y, this.rotation.x, 0);
            var deltaTransform = BABYLON.Vector3.TransformCoordinates(new BABYLON.Vector3(this._leftjoystick.deltaPosition.x * speed, this._leftjoystick.deltaPosition.y * speed, this._leftjoystick.deltaPosition.z * speed), cameraTransform);
            this.cameraDirection = this.cameraDirection.add(deltaTransform);
            this.cameraRotation = this.cameraRotation.addVector3(this._rightjoystick.deltaPosition);
            if (!this._leftjoystick.pressed) {
                this._leftjoystick.deltaPosition = this._leftjoystick.deltaPosition.scale(0.2);
            }
            if (!this._rightjoystick.pressed) {
                this._rightjoystick.deltaPosition = this._rightjoystick.deltaPosition.scale(0.2);
            }
            _super.prototype._checkInputs.call(this);
        };
        CustomJoysticksCamera.prototype.dispose = function () {
            this._leftjoystick.releaseCanvas();
            _super.prototype.dispose.call(this);
        };
        CustomJoysticksCamera.prototype.serialize = function () {
            var serializationObject = _super.prototype.serialize.call(this);
            serializationObject.type = "CustomJoysticksCamera";
            return serializationObject;
        };
        return CustomJoysticksCamera;
    })(BABYLON.FreeCamera);
    BABYLON.CustomJoysticksCamera = CustomJoysticksCamera;
})(BABYLON || (BABYLON = {}));