<div id="content">
    <div id="head-content">
        <h2>Работа с накладными</h2>
    </div>
    <div id="content-body">
        <fieldset class="labels one">
            <div id="content-invoice">
                <table cellspacing="1">
                    <colgroup colspan="1" width="35%"></colgroup>
                    <colgroup colspan="1" width="20%"></colgroup>
                    <colgroup colspan="1" width="30%"></colgroup>
                    <colgroup colspan="1" width="15%"></colgroup>
                    <?php
                    include_once "../../common_files/functions.php";
                    mysql_connect($mysqlparams['server'], $mysqlparams['user'], $mysqlparams['password']);
                    mysql_select_db($mysqlparams['database']);
                    $query = "SELECT `invoice`.`InvoiceID`, `invoice`.`InvoiceNumber`, `invoice`.`Date`, `users`.`FirstName`, `users`.`LastName` FROM `invoice`, `users` WHERE `invoice`.`isEnabled`=1 AND `invoice`.`LastModBy` = `users`.`UserID`";
                    $result = mysql_query($query);
                    if (!$result) {
                        echo "								<tr><td colspan=4>Подобных пунктов нет..</td></tr>\n";
                    } else {
                        echo "								<tr><th>Накладная</th><th>Дата создания</th><th>Создатель</th><th>Действия</th></tr>\n";
                        while ($row = mysql_fetch_array($result)) {
                            echo '								<tr><td>' . $row["InvoiceNumber"] . '</td><td>' . $row["Date"] . '</td><td>' . $row["FirstName"] . $row["LastName"] . '</td><td>' . '<button value="' . $row['InvoiceID'] . '">удалить</button>' . "</td></tr>\n";
                        }
                    }
                    mysql_close();
                    ?>
                </table>
            </div>
        </fieldset>
        <div class="end-of-content"></div>
    </div>
    <div class="end-of-content"></div>
</div>
<script type="text/javascript">
    for (var i = 0; i < document.getElementsByTagName('button').length; i++) {
        document.getElementsByTagName('button')[i].onclick = function () {
            var newQuery = new XMLHttpRequest();
            newQuery.open('post', 'documents.php', true);
            newQuery.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            var status = 'removeInvoice&InvoiceID=' + this.value;
            newQuery.send('Status=' + status);
            newQuery.onreadystatechange = function () {
                if (newQuery.readyState != 4) return;
                var result = this.responseText;
                console.log(result);
            }
            this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode);
        }
    }
</script>