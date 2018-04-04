### Тесты:

1.	После клика по ссылке «Manage Jenkins» на странице появляется элемент dt с текстом «Manage Users» и элемент dd с текстом «Create/delete/modify users that can log in to this Jenkins».
2.	После клика по ссылке, внутри которой содержится элемент dt с текстом «Manage Users», становится доступна ссылка «Create User».
3.	После клика по ссылке «Create User» появляется форма с тремя полями типа text и двумя полями типа password, причём все поля должны быть пустыми.
4.	После заполнения полей формы («Username» = «someuser», «Password» = «somepassword», «Confirm password» = «somepassword», «Full name» = «Some Full Name», «E-mail address» = «some@addr.dom») и клика по кнопке с надписью «Create User» на странице появляется строка таблицы (элемент tr), в которой есть ячейка (элемент td) с текстом «someuser».
5.	После клика по ссылке с атрибутом href равным «user/someuser/delete» появляется текст «Are you sure about deleting the user from Jenkins?».
6.	После клика по кнопке с надписью «Yes» на странице отсутствует строка таблицы (элемент tr), с ячейкой (элемент td) с текстом «someuser». На странице отсутствует ссылка с атрибутом href равным «user/someuser/delete».
7.	{На той же странице, без выполнения каких бы то ни было действий}. На странице отсутствует ссылка с атрибутом href равным «user/admin/delete».