<?php 
 
 //Constants for database connection
 define('DB_HOST','101.50.1.56');
 define('DB_USER','bryanyeh_user');
 define('DB_PASS','H@F6wmPKk6Rw7@V');
 define('DB_NAME','bryanyeh_database');
 define('UPLOAD_PATH', 'uploadfile/');
 
 //connecting to database 
 $conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to connect');
 $response = array();
 if(isset($_GET['apicall'])){
 switch($_GET['apicall']){
 
 //if it is an upload call we will upload the file
 case 'uploadfile':
 
 if(isset($_FILES['file']['name']) && isset($_POST['date'])){
 try{
 move_uploaded_file($_FILES['file']['tmp_name'], UPLOAD_PATH . $_FILES['file']['name']);
 $stmt = $conn->prepare("INSERT INTO files (file, date) VALUES (?,?)");
 $stmt->bind_param("ss", $_FILES['file']['name'],$_POST['date']);
 if($stmt->execute()){
 $response['error'] = false;
 $response['message'] = 'File Sukses Terupload';
 }else{
 throw new Exception("Tidak Bisa Mengupload File");
 }
 }catch(Exception $e){
 $response['error'] = true;
 $response['message'] = 'Tidak Bisa Mengupload File';
 }
 
 }else{
 $response['error'] = true;
 $response['message'] = "Ada Parameter Yang Kurang";
 }
 break;
 
 //in this call we will fetch all the files 
 case 'getfiles':
 
 $server_ip = gethostbyname(gethostname());
 $stmt = $conn->prepare("SELECT id, file, date FROM files");
 $stmt->execute();
 $stmt->bind_result($id, $file, $date);
 $files = array();
 
 while($stmt->fetch()){
 $temp = array();
 $temp['id'] = $id; 
 $temp['file'] = 'http://' . $server_ip . '/upload/'. UPLOAD_PATH . $file; 
 $temp['date'] = $date; 
 
 array_push($files, $temp);
 }
 
 //pushing the array in response 
 $response['error'] = false;
 $response['files'] = $files; 
 break; 
 
 default: 
 $response['error'] = true;
 $response['message'] = 'Invalid api call';
 }
 
 }else{
 header("HTTP/1.0 404 Not Found");
 echo "<h1>Connected</h1>";
 echo "The Database Connection is Successful.";
 exit();
 }
 
 //displaying the response in json 
 header('Content-Type: application/json');
 echo json_encode($response);
 