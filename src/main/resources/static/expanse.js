let app = angular.module('expanseApp', ['ngCookies']);

// Expanse factory and service
app.factory('expanseFactory', function ($http) {
    let service = {};

    let baseUrl = 'http://localhost:8080/command/request?cmd=';
    let _finalUrl = '';
    let _command = '';

    let makeUrl = function () {
        _finalUrl = baseUrl + _command;

        return _finalUrl;
    };

    service.requestCommand = function (user, command) {
        _command = command;

        makeUrl();

        return $http({
            method: 'PUT',
            url: _finalUrl,
            headers: {
                "X-Expanse-User": user
            }
        });
    };

    service.readyPlayerOne = function(user) {
        let baseURL = 'http://localhost:8080/player/readyplayerone';
        return $http({
            method: 'GET',
            url: baseURL,
            headers: {
                "X-Expanse-User": user
            }
        });
    };

    service.link = function(user, link) {
        return $http({
            method: 'GET',
            url: link,
            headers: {
                "X-Expanse-User": user
            }
        });
    };

    return service;
});

// Expanse controller
app.controller('expanseController', function ($scope, $cookies, expanseFactory) {
    let MESSAGE_DETAIL_INDEX = 0;
    let REF_DETAIL = 'detail';
    let REF_LOCATION = 'location';

    $scope.data = {
        messages:["Welcome traveler..."],
        command:"look",
        inventory:[],
        location:{},
        history:[],
        market:[],
        showInventory:false,
        showHistory:false,
        showMarket:false,
        player:null
    };

    $scope.showMapPosition = function() {
        if ( $scope.data.location.mapx !== undefined ) {
            return {
                "background-position": "left -" + $scope.data.location.mapx + "px top -" + $scope.data.location.mapy + "px"
            };
        } else {
            return {};
        }
    };

    /**
     * Submit a command to the expanse engine.
     */
    $scope.submitCommand = function () {
        expanseFactory.requestCommand($scope.data.player, $scope.data.command)
            .then(function (response) {
                // Keep track of history (only first message)
                $scope.data.history.unshift($scope.data.messages[0]);
                if ( $scope.data.history.length > 5 ) {
                    $scope.data.history.pop();
                }

                // Reset the messages
                $scope.data.messages = [""];
                $scope.data.transition = null;
                $scope.data.locationitem = null;

                // And the new message
                addMessage(response.data.result, true);

                // Processing links based on link 'rel'ationships.
                if ( response.data !== undefined && response.data.links.length > 0 ) {
                    $scope.processLinks($scope.data.player, response.data.links);
                }
            }, function (data) {
                addMessage("Good grief, something is seriously messed up.");
                addMessage(data);
            });

        $scope.data.command = "";
    };

    $scope.showInventory = function() {
        $scope.data.showInventory = !$scope.data.showInventory;
    };

    $scope.showHistory = function() {
        $scope.data.showHistory = !$scope.data.showHistory;
    };

    /**
     * Process all hateoas links for the request.
     *
     * @param links The provided links.
     */
    $scope.processLinks = function(user, links) {
        links.forEach(link => {
            expanseFactory.link(user, link.href).then(function(response) {
                if ( link.rel == REF_DETAIL ) {
                    addMessage(response.data.result, true);
                } else if ( typeof response.data !== 'undefined' ) {
                     $scope.data[link.rel] = response.data.result;
                }

                // Keep processing links until all have finished.
                if ( response.data !== undefined
                    && response.data.links !== undefined
                    && response.data.links.length > 0 ) {
                    $scope.processLinks(user, response.data.links);
                }
            });
        })
    };

    /**
     *
     * @param message
     */
    function addMessage(message, append) {
        if ( append ) {
            $scope.data.messages[MESSAGE_DETAIL_INDEX] += $scope.data.messages.length == 0 ?
                message : ' ' + message;
        } else {
            $scope.data.messages.push(message);
        }
    }

    function listAllInventory() {
        $scope.data.inventory.forEach(i => {
            addMessage(i.description);
        });
    }

    function init() {
        var playerOneName = $cookies.get("expanse.player.one");
        if ( playerOneName === undefined ) {
            playerOneName = '';
        }

        expanseFactory.readyPlayerOne(playerOneName).then(function(response) {
            playerOneName = response.data.name;

            $cookies.put("expanse.player.one", playerOneName);

            $scope.data.player = playerOneName;

            $scope.submitCommand();
        }), function(error) {
            windows.console(error);
        };
    }

    init();
});

app.directive('commandInterface', function() {
    return {
        restrict: 'E',
        templateUrl: 'command.html'
    };
});

app.directive('inventory', function() {
    return {
        restrict: 'E',
        scope:{
            inventory:"=items",
            showInventory:"=show"
        },
        templateUrl: 'inventory.html'
    };
});