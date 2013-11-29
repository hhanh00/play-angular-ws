app = angular.module('app', [
  'ngRoute'
  'util'
  ])

util = angular.module('util', [])
util.filter("timer", ["time", (time) ->
  (input) ->
    if input
      seconds=toSeconds(input)
      minutes=toMinutes(input)
      hours=toHours(input)
      "#{hours}:#{minutes}:#{seconds}"
    else
      "Press Start"
  ])
  
util.service("time", ->
  @toHours = (timeMillis) -> addZero((timeMillis/(1000*60*60)))
  @toMinutes = (timeMillis) -> addZero((timeMillis/(1000*60))%60)
  @toSeconds = (timeMillis) -> addZero((timeMillis/1000)%60)
  @toTime = (hours,minutes,seconds) -> ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000
  @addZero = (value) ->
    value = Math.floor(value)
    if(value < 10)
      "0#{value}"
    else
      value
  )
  
util.controller("TimerController", ["$scope", "$http", ($scope, $http) ->
 startWS = ->

  $scope.start = ->
    $http.get(jsRoutes.controllers.AppController.start().url).success( -> )

  $scope.stop = ->
    $http.get(jsRoutes.controllers.AppController.stop().url).success( -> )

  startWS()
  ])