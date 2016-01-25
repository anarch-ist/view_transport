$(document).ready(function() {

    // example data for exchange with server
    var exampleData = [{userID: 1, firstName:"wefwfe", lastName:"ewrkbfif", position: "efewerfw", patronymic:"ergerge", phoneNumber: "9055487552",
        email: "qwe@qwe.ru", password:"lewrhbwueu23232", userRoleRusName:"Диспетчер", pointName:"point1"}];


    // загрузка всех ролей пользователя с сервера
    $.post( "content/getData.php",
        {status: "getAllUserRoles", format:"json"},
        // server returns array of pairs [{userRoleID:'ADMIN', userRoleRusName:'Администратор'}, {userRoleID:'W_DISPATCHER', userRoleRusName:'Диспетчер_склада'}]
        function(userRolesData) {
            var options = [];
            var selectizeOptions = [];
            userRolesData = JSON.parse(data);
            userRolesData.forEach(function(entry) {
                var option = "<option value=" + entry.userRoleID+">" + entry.userRoleRusName + "</option>";
                options.push(option);
                var selectizeOption = { "label": entry.userRoleRusName, "value": entry.userRoleID };
                selectizeOptions.push(selectizeOption);
            });
            var userRoleSelectize = usersEditor.field('role').inst();
            userRoleSelectize.clear();
            userRoleSelectize.clearOptions();
            userRoleSelectize.load(function(callback) {
                callback(selectizeOptions);
            });
        });

    var usersEditor = new $.fn.dataTable.Editor( {
        ajax: {
            create: {
                type: 'POST',
                url: 'content/getData.php'
            },
            edit: {
                type: 'PUT',
                url: 'content/getData.php'
            },
            remove: {
                type: 'DELETE',
                url: 'content/getData.php'
            }
        },
        table: '#usersTable',
        idSrc: 'userID',

        fields: [
            { label: 'Имя', name: 'firstName', type: 'text'},
            { label: 'Фамилия',  name: 'lastName', type: 'text'},
            { label: 'Отчество',  name: 'patronymic', type: 'text'},
            { label: 'Должность',  name: 'position', type: 'text'},
            { label: 'Номер телефона',  name: 'phoneNumber', type: 'mask', mask:"(000) 000-00-00", maskOptions: {clearIfNotMatch: true}, placeholder:"(999) 999-99-99"},
            { label: 'Почта',  name: 'email', type: 'text'},
            { label: 'Пароль',  name: 'password', type: 'password'},
            { label: 'Роль',  name: 'role', type: 'selectize'},
            { label: 'Пункт',  name: 'point', type: 'selectize'}
        ]
    } );



    // максимально может быть несколько сотен пользователей
    var $usersDataTable =  $("#usersTable").DataTable({
            "dom": 'Bt', // show only buttons and table with no decorations
            language: {
                url:'/localization/dataTablesRus.json'
            },
            data: exampleData,
            select: {
                style: 'single'
            },
            "buttons": [
                {
                    extend: "create",
                    editor: usersEditor,
                    text: 'добавить запись'
                },
                {
                    extend: "remove",
                    editor: usersEditor,
                    text: 'удалить запись'
                },
                {
                    extend: "edit",
                    editor: usersEditor,
                    text: "изменить"
                }
            ],
            "paging": 10,
            "columnDefs": [
                {"name": "firstName", "data": "firstName", "targets": 0},
                {"name": "lastName", "data": "lastName", "targets": 1},
                {"name": "patronymic", "data": "patronymic", "targets": 2},
                {"name": "position", "data": "position", "targets": 3},
                {"name": "phoneNumber", "data": "phoneNumber", "targets": 4},
                {"name": "email", "data": "email", "targets": 5},
                {"name": "password", "data": "password", "targets": 6},
                {"name": "userRoleRusName", "data": "userRoleRusName", "targets": 7},
                {"name": "pointName", "data": "pointName", "targets": 8}
            ]
        }
    );
});