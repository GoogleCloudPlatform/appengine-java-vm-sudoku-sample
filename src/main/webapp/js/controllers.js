'use strict';

(function(){
  var controllers = angular.module("controllers", []);
  controllers.controller('SudokuController', ['$http', '$log', function($http, $log){
    
    var vm = this;
    
    this.uploadResult = {};
    this.fileData = {};
    
    this.uploadFile = function(file, uploadUrl){
      vm.uploadResult = {};
      $log.info("about to :");
      var fd = new FormData();
      fd.append('image_file', file);
      $http.post(uploadUrl, fd, {
          transformRequest: angular.identity,
          headers: {'Content-Type': undefined}
      })
      .success(function(data, status, headers, config){
        var ct = headers('Content-Type');
        $log.info(ct);
        if (typeof ct != 'undefined' && ct.indexOf("text/") > -1) {
          vm.uploadResult.errorContent = data;
        } else {
          vm.uploadResult.response = 'data:image/png;base64,' + data;
        }
      })
      .error(function(data){
        $log.info("error");
      });
    }
  }]);

})();
