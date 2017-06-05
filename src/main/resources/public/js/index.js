var teachers_table = new Vue({
    el: '#teachers-table',
    data: {
        searchNum: '',
        teachers: [],
        sexes: ['男', '女'],
        insertTeacher: {
            teacherNum: '',
            teacherName: '',
            sex: '男',
            title: ''
        },
        updateTeacher: {
            teacherNum: '',
            teacherName: '',
            sex: '',
            title: ''
        },
        searchResult: '没有找到要求搜索的内容'
    },
    mounted: function () {
        var vm = this;
        $.ajax('teachers/getAllTeachers', {
            method: 'GET'
        }).done(function (data) {
            vm.teachers = data;
        })
    },
    methods: {
        insertChangeSex: function (event) {
            var target = event.target;
            this.insertTeacher.sex = target.innerText;
        },
        updateChangeSex: function (event) {
            var target = event.target;
            this.updateTeacher.sex = target.innerText;
        },
        insertTeacherFunc: function () {
            var vm = this;
            var flag = false;
            this.teachers.forEach(function (element) {
                if (vm.insertTeacher.teacherNum === element.teacherNum) {
                    flag = true;
                }
            });

            if(!flag){
                $.ajax('teachers/insertTeacher', {
                    method: 'POST',
                    data: JSON.stringify(vm.insertTeacher),
                    contentType: "application/json"
                }).done(function (data) {
                    if (data === "success") {
                        swal("数据添加成功！", "你已成功添加所选教师数据", "success");
                        vm.teachers.push(JSON.parse(JSON.stringify(vm.insertTeacher)));    // 要一份对象的深拷贝
                        $('div#insertTeacher').modal('hide');
                        vm.insertTeacher.teacherNum = vm.insertTeacher.teacherName = vm.insertTeacher.title = '';
                        vm.insertTeacher.sex = '男';
                    } else {
                        console.log("wrong");
                    }

                }).fail(function (xhr, status) {
                    console.log('失败: ' + xhr.status + ', 原因: ' + status);
                });
            } else {
                swal("数据添加失败！","教师工号不得重复！", "error");
            }

        },
        readyUpdate: function (teacher) {
            this.updateTeacher.teacherNum = teacher.teacherNum;
            this.updateTeacher.teacherName = teacher.teacherName;
            this.updateTeacher.sex = teacher.sex;
            this.updateTeacher.title = teacher.title;
        },
        updateTeacherFunc: function () {
            var vm = this;

            $.ajax('teachers/updateTeacher', {
                method: 'POST',
                data: JSON.stringify(vm.updateTeacher),
                contentType: "application/json"
            }).done(function (data) {
                if (data === "success") {
                    swal("数据修改成功！", "你已成功修改所选教师数据", "success");
                    vm.teachers.forEach(function (element, index) {
                        if (element.teacherNum === vm.updateTeacher.teacherNum) {
                            vm.teachers.splice(index, 1, JSON.parse(JSON.stringify(vm.updateTeacher)));
                        }
                    });
                    $('div#updateTeacher').modal('hide');
                } else {
                    console.log("wrong");
                }

            }).fail(function (xhr, status) {
                console.log('失败: ' + xhr.status + ', 原因: ' + status);
            });

        },
        deleteTeacherFunc: function (teacher) {

            var vm = this;
            $.ajax('teachers/deleteTeacher', {
                method: 'GET',
                data: {
                    teacherNum: teacher.teacherNum
                }

            }).done(function (data) {
                if (data === "success") {
                    swal("数据删除成功！", "你已成功删除所选教师数据", "success");
                    vm.teachers.forEach(function (element, index) {
                        if (element.teacherNum === teacher.teacherNum) {
                            vm.teachers.splice(index, 1);
                        }
                    });
                } else {
                    console.log("wrong");
                }
            }).fail(function (xhr, status) {
                console.log('失败: ' + xhr.status + ', 原因: ' + status);
            });

        },

        searchTeacherFunc: function () {

            var vm = this;
            this.teachers.forEach(function (element) {
                if (element.teacherNum === vm.searchNum) {
                    // 展示
                    vm.searchResult = "教师工号：" + element.teacherNum + "\n姓名：" + element.teacherName + "\n性别：" + element.sex + "\n职称：" + element.title;
                }
            });

            swal("教师信息", vm.searchResult);
        }
    }
});