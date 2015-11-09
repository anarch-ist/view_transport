<?php

include 'Db.php';
/* Database connection start */
$servername = "localhost";
$username = "andy";
$password = "andyandy";
$dbname = "project_database";

$conn = mysqli_connect($servername, $username, $password, $dbname) or die("Connection failed: " . mysqli_connect_error());

//$db = new Db();
//
//$rows = $db->select("SELECT * from `employee`");
//
//foreach($rows as $row) {
//    echo $row[0];
//}





//$conn->query("CALL selectData");
//$result = mysqli_use_result($conn);

//if ($result === false) {
//    // Handle failure - log the error, notify administrator, etc.
//} else {
//    // Fetch all the rows in an array
//    $rows = array();
//    while ($row = mysqli_fetch_assoc($result)) {
//        $rows[] = $row;
//    }
//}




//while ($row = $result->mysqli_fetch_assoc()) {
//    printf("%d\n", $row['номер заявки']);
//}
//mysqli_free_result($result);



//if ($conn->query("CALL selectData"))
//    echo "CALL failed: (" . $conn->errno . ") " . $conn->error;





/* Database connection end */


// storing  request (ie, get/post) global array to a variable  
$requestData = $_REQUEST; // contains data that was in get or post request from datatables

$columns = array(
// datatable column index  => database column name
    0 => 'employee_name',
    1 => 'employee_salary',
    2 => 'employee_age'
);

// getting total number records without any search
$sql = "SELECT employee_name, employee_salary, employee_age ";
$sql .= " FROM employee";
$query = mysqli_query($conn, $sql) or die("employee-grid-data.php: get employees");
$totalData = mysqli_num_rows($query);
$totalFiltered = $totalData;  // when there is no search parameter then total number rows = total number filtered rows.


$sql = "SELECT employee_name, employee_salary, employee_age ";
$sql .= " FROM employee WHERE 1=1";
if (!empty($requestData['search']['value'])) {   // if there is a search parameter, $requestData['search']['value'] contains search parameter
    $sql .= " AND ( employee_name LIKE '" . $requestData['search']['value'] . "%' ";
    $sql .= " OR employee_salary LIKE '" . $requestData['search']['value'] . "%' ";

    $sql .= " OR employee_age LIKE '" . $requestData['search']['value'] . "%' )";
}
$query = mysqli_query($conn, $sql) or die("employee-grid-data.php: get employees");
$totalFiltered = mysqli_num_rows($query); // when there is a search parameter then we have to modify total number filtered rows as per search result. 
$sql .= " ORDER BY " . $columns[$requestData['order'][0]['column']] . "   " . $requestData['order'][0]['dir'] . "  LIMIT " . $requestData['start'] . " ," . $requestData['length'] . "   ";
/* $requestData['order'][0]['column'] contains colmun index, $requestData['order'][0]['dir'] contains order such as asc/desc  */
$query = mysqli_query($conn, $sql) or die("employee-grid-data.php: get employees");

$data = array();
while ($row = mysqli_fetch_array($query)) {  // preparing an array
    $nestedData = array();

    $nestedData[] = $row["employee_name"];
    $nestedData[] = $row["employee_salary"];
    $nestedData[] = $row["employee_age"];

    $data[] = $nestedData;
}


$json_data = array(
    "draw" => intval($requestData['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
    "recordsTotal" => intval($totalData),  // total number of records
    "recordsFiltered" => intval($totalFiltered), // total number of records after searching, if there is no searching then totalFiltered = totalData
    "data" => $data   // total data array
);

echo json_encode($json_data);  // send data as json format

?>
