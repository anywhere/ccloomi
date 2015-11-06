/**
 * Created by chenxianjun on 15/10/24.
 */
angular.module('ccloomi')
    .factory('S_user', ['$http','$location','$timeout',function ($http,$location,$timeout) {
        var service={
                //user:{},
                //views:[],
                //roles:[],
                //permissions:[],
                setName: function (newName) {
                    service.user['name']=newName;
                },
                save:function(){},
                hasRole:function(role){
                    return service.roles.indexOf(role)==-1?false:true;
                },
                hasPermission:function(permission){
                    return service.permissions.indexOf(permission)==-1?false:true;
                },
                login: function (user,callback,from) {
                	from=from||'/main';
                    $http.post('sys/login.xhtml',user).success(function(data){
                        if(data.code==0){
                            service.user=data.info.user;
                            service.views=data.info.views;
                            service.roles=data.info.roles;
                            service.permissions=data.info.permissions;
                            callback();
                            $timeout(function () {
                                $location.path(from);
                            },1000);
                        }else{
                            swal('登录失败',data.info,'error');
                        }
                    });
                },
                currentUser: function (scope) {
                    $http.post('sys/currentUser.json').success(function (data) {
                        service.user=data.user;
                        service.views=data.views;
                        service.roles=data.roles;
                        service.permissions=data.permissions;
                        scope.views=data.views;
                    })
                }

            };
            return service;
        }])
    .factory('S_pagination',['$http', function ($http) {
        var service={
            pagination: function (paginationContainer,dataUrl,pageSize,callback,data,beforeSend) {
                paginationContainer.pagination({
                    dataSource: dataUrl,
                    locator: 'data',
                    pageSize: pageSize,
                    ajax: {
                        type:'POST',
                        data:data,
                        contentType:'application/json ;charset=UTF-8',
                        beforeSend: beforeSend|| function () {}
                    },
                    callback: callback
                })
            }
        };
        return service;
    }])