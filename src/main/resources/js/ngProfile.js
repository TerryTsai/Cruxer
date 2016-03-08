(function () {

    var app = angular.module("profile", []);

    app.controller("AccountCtrl", ['$scope', '$http', function ($scope, $http) {
        $scope.username = "";
        $scope.date = "";
        $scope.email = "";
        $scope.firstname = "";
        $scope.lastname = "";

        $http
            .get("/profile/account")
            .then(function (result) {
                $scope.username = result.data.username;
                $scope.date = result.data.date;
                $scope.email = result.data.email;
                $scope.firstname = result.data.firstname;
                $scope.lastname = result.data.lastname;
            });

    }]);

    app.controller("RouteCtrl", ['$scope', '$http', function ($scope, $http) {

        $scope.page = 0;
        $scope.size = 3;
        $scope.routes = [];

        $http.get("/profile/routes?page=" + $scope.page + "&size=" + $scope.size)
            .then(function (result) {$scope.routes = result.data;});

        $scope.nextPage = function () {
            $http.get("/profile/routes?page=" + ($scope.page + 1) + "&size=" + $scope.size)
                .then(function (result) {
                    if (result.data.length > 0) {
                        $scope.routes = result.data;
                        $scope.page++;
                    }
                });
        };

        $scope.prevPage = function () {
            if ($scope.page <= 0) return;
            $http.get("/profile/routes?page=" + ($scope.page - 1) + "&size=" + $scope.size)
            .then(function (result) {
                if (result.data.length > 0) {
                    $scope.routes = result.data;
                    $scope.page--;
                }
            });
        };

    }]);

    app.controller("CommentCtrl", ['$scope', '$http', function ($scope, $http) {

        $scope.page = 0;
        $scope.size = 5;
        $scope.comments = [];

        $http.get("/profile/comments?page=" + $scope.page + "&size=" + $scope.size)
            .then(function (result) {$scope.comments = result.data;});

        $scope.nextPage = function () {
            $http.get("/profile/comments?page=" + ($scope.page + 1) + "&size=" + $scope.size)
                .then(function (result) {
                    if (result.data.length > 0) {
                        $scope.comments = result.data;
                        $scope.page++;
                    }
                });
        };

        $scope.prevPage = function () {
            if ($scope.page <= 0) return;
            $http.get("/profile/comments?page=" + ($scope.page - 1) + "&size=" + $scope.size)
                .then(function (result) {
                    if (result.data.length > 0) {
                        $scope.comments = result.data;
                        $scope.page--;
                    }
                });
        };

    }]);

})();