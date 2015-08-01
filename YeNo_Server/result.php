<?php

	$response = array();

	// check for required fields
	if (isset($_GET['uid'])) {

		$n = $_GET['uid'];

		// include db connect class
		require_once __DIR__ . '/db_connect.php';

		// connecting to db
		$db = new DB_CONNECT();

		// mysql inserting a new row
		$result  = mysql_query("select * from questions where uid='$n'");
		if ($result) {
			while ($row = mysql_fetch_array($result)) {
				$response["success"]     = 1;
				$response["yes"]         = $row['yescount'];
				$response["no"]         = $row['nocount'];
			}
			echo json_encode($response);
		} else {
			// failed to insert row
			$response["success"] = 0;
			$response["message"] = "Oops! An error occurred.";

			echo json_encode($response);
		}
	} else {
		// required field is missing
		$response["success"] = 0;
		$response["message"] = "Required field(s) is missing";

		echo json_encode($response);
	}
?>
