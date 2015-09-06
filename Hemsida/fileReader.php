<?php

//header('Content-type: text/html; charset=utf-8');

$myfile = fopen("data.txt", "r") or die("Unable to open file!");
$value = array();

while (!feof($myfile)) {
	$value[] = fgets($myfile);

}

/*for ($i = 0; $i <= 1; $i++) {
$rainAndTemp =  explode(" ", $value[$i]) . "<br>";
}
*/
  //  echo "The number is: $x <br>";


var_dump($value);
//var_dump($rainAndTemp);

fclose($myfile);

?>
