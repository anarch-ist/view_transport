<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>TEST</title>
        <link rel="stylesheet" type="text/css" href="media/css/jquery.dataTables.min.css">
        <script type="text/javascript" language="javascript" src="media/js/jquery.js"></script>
        <script type="text/javascript" language="javascript" src="media/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" language="javascript" >
            $(document).ready(function() {
                var dataTable = $('#employee-grid').DataTable( {
                    "processing": true,
                    "serverSide": true,
                    "ajax":{
                        url :"employee-grid-data.php", // json datasource
                        type: "post",  // method  , by default get
                        error: function(){  // error handling
                            $(".employee-grid-error").html("");
                            $("#employee-grid").append('<tbody class="employee-grid-error"><tr><th colspan="3">No data found in the server</th></tr></tbody>');
                            $("#employee-grid_processing").css("display","none");

                        }
                    }
                } );
            } );
        </script>


    </head>
    <body>
        <table id="employee-grid"  cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
            <thead>
                <tr>
                    <th>Employee name</th>
                    <th>Salary</th>
                    <th>Age</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </body>
</html>