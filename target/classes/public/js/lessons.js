var lessons_table = new Vue({
    el: '#lessons-table',
    data: {
        searchNum: '',
        lessons: [],
        teachers: [],
        terms: ['上', '下'],
        insertLesson: {
            lessonNum: '',
            lessonName: '',
            teacher: {},
            year: '',
            term: '上',
            credit: 0
        },
        updateLesson: {
            lessonNum: '',
            lessonName: '',
            teacher: {},
            year: '',
            term: '上',
            credit: 0
        },
        updateLessonsTime: {
            year: '',
            term: '上'
        },
        searchResult: '没有找到要求搜索的内容',
        highestCredit: null
    },
    mounted: function () {
        var vm = this;
        $.ajax('lessons/getAllLessons', {
            method: 'GET'
        }).done(function (data) {
            vm.lessons = data;
        }).fail(function (xhr, status) {
            console.log('失败: ' + xhr.status + ', 原因: ' + status);
        });

    },
    methods: {
        getAllTeachers: function () {

            var vm = this;

            $.ajax('teachers/getAllTeachers', {
                method: 'GET'
            }).done(function (data) {
                vm.teachers = data;
            })
        },
        insertChangeTerm: function (event) {
            var target = event.target;
            this.insertLesson.term = target.innerText;
        },
        updateChangeTerm: function (event) {
            var target = event.target;
            this.updateLesson.term = target.innerText;
        },
        updateLessonsTimeChangeTerm: function (event) {
            var target = event.target;
            this.updateLessonsTime.term = target.innerText;
        },
        updateLessonsTimeFunc: function () {
            var vm = this;

            $.ajax('lessons/updateLessonsTime', {
                method: 'POST',
                data: JSON.stringify(vm.updateLessonsTime),
                contentType: "application/json"
            }).done(function (data) {
                vm.lessons.forEach(function (element) {
                    element.year = vm.updateLessonsTime.year;
                    element.term = vm.updateLessonsTime.term;
                });
                swal("课程时间已更新！", "你已成功更新所有课程的学年与学期", "success");
                $('div#updateLessonsTime').modal('hide');

            });

        },
        addCreditTrigger: function () {
            var vm = this;

            $.ajax('lessons/addCreditTrigger', {
                method: 'GET',
                data: {
                    credit: vm.highestCredit
                }
            }).done(function (data) {
                if (data === "success") {
                    swal("学分限制添加成功！","所有新增课程学分不得大于此值", "success");
                } else {
                    console.log("wrong");
                }
            });
        },
        insertLessonFunc: function () {

            var vm = this;

            $.ajax('lessons/insertLesson', {
                method: 'POST',
                data: JSON.stringify(vm.insertLesson),
                contentType: "application/json"
            }).done(function (data) {
                if (data === "success") {
                    swal("数据添加成功！", "你已成功添加所选课程数据", "success");
                    vm.lessons.push(JSON.parse(JSON.stringify(vm.insertLesson)));    // 要一份对象的深拷贝
                    $('div#insertLesson').modal('hide');
                    vm.insertLesson.lessonNum = vm.insertLesson.lessonName = vm.insertLesson.year = '';
                    vm.insertLesson.teacher = {};
                    vm.insertLesson.credit = 0;
                    vm.insertLesson.term = '上';
                } else {
                    console.log("wrong");
                }

            }).fail(function (xhr, status) {
                swal("数据添加失败！","学分大于最高限制，不合要求！", "error");
            });

        },

        readyUpdate: function (lesson) {
            this.updateLesson.lessonNum = lesson.lessonNum;
            this.updateLesson.lessonName = lesson.lessonName;
            this.updateLesson.teacher = lesson.teacher;
            this.updateLesson.year = lesson.year;
            this.updateLesson.term = lesson.term;
            this.updateLesson.credit = lesson.credit;
            this.getAllTeachers();
        },

        updateLessonFunc: function () {
            var vm = this;

            $.ajax('lessons/updateLesson', {
                method: 'POST',
                data: JSON.stringify(vm.updateLesson),
                contentType: "application/json"
            }).done(function (data) {
                if (data === "success") {
                    swal("数据修改成功！", "你已成功修改所选课程数据", "success");
                    vm.lessons.forEach(function (element, index) {
                        if (element.lessonNum === vm.updateLesson.lessonNum) {
                            vm.lessons.splice(index, 1, JSON.parse(JSON.stringify(vm.updateLesson)));
                        }
                    });
                    $('div#updateLesson').modal('hide');
                } else {
                    console.log("wrong");
                }

            }).fail(function (xhr, status) {
                console.log('失败: ' + xhr.status + ', 原因: ' + status);
            });

        },

        deleteLessonFunc: function (lesson) {

            var vm = this;

            $.ajax('lessons/deleteLesson', {
                method: 'GET',
                data: {
                    lessonNum: lesson.lessonNum
                }

            }).done(function (data) {
                if (data === "success") {
                    swal("数据删除成功！", "你已成功删除所选课程数据", "success");
                    vm.lessons.forEach(function (element, index) {
                        if (element.lessonNum === lesson.lessonNum) {
                            vm.lessons.splice(index, 1);
                        }
                    });

                } else {
                    console.log("wrong");
                }

            }).fail(function (xhr, status) {
                console.log('失败: ' + xhr.status + ', 原因: ' + status);
            });

        },

        searchLessonFunc: function () {

            var vm = this;

            this.lessons.forEach(function (element, index) {
                if (element.lessonNum === vm.searchNum) {
                    // 展示
                    vm.searchResult = "课程号：" + element.lessonNum + "\n课程名：" + element.lessonName +
                        "\n任课老师：" + element.teacher.teacherName + "\n学年：" + element.year +
                        "\n学期：" + element.term + "\n学分：" + element.credit;
                }
            });

            swal("课程信息", vm.searchResult);
        }
    }
});