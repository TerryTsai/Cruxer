/**
 * Created by Terry on 3/6/2016.
 */

var API = (function($, a) {

    a.postAccount = function(form, submit) {
        $.ajaxSubmit({
            url: "accounts",
            type: "post",
            data: $(form).serialize(),
            success: function (e) {
                submit.html("Registered.");
                submit.prop("disabled", true);
            },
            error: function (e) {
                submit.html("Try Again?");
            }
        });
    };

    a.postHold = function(form, submit) {
        $.ajaxSubmit({
            url: "holds",
            type: "post",
            dataType: "json",
            success: function (e) {
                submit.html("Registered.");
                submit.prop("disabled", true);
            },
            error: function (e) {
                submit.html("Try Again?");
            }
        })
    };

    a.postWall = function(form, submit) {
        $.ajaxSubmit({
            url: "walls",
            type: "post",
            dataType: "json",
            success: function (e) {
                submit.html("Registered.");
                submit.prop("disabled", true);
            },
            error: function (e) {
                submit.html("Try Again?");
            }
        })
    };

    return a;

}($, API || {}));