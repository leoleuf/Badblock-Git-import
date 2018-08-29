<?php

chdir('/home/web/dev-web/badblock-website');
shell_exec('git pull http://websyncbot:WAK2tRsB5jMzredQJ9uYxqyZwQUM6eYWTP74RJPM4rVvrCapLjf4F5SVHrqNkY4X@lusitania.badblock.fr/Website/badblock-website.git');
shell_exec('php composer.phar install');
shell_exec('php composer.phar update');

$data = array("username" => "Logger Site","embeds" => array(0 => array(
    "url" => "https://dev-web.badblock.fr",
    "title" => "GitLab Puller ",
    "description" => "Site Web de démo update !",
    "color" => 65280
)));

#$curl = curl_init("https://discordapp.com/api/webhooks/455466832871292928/TazyKTAJ_DIaMpciQOmriVP5aiaa3pM8E7rIFBHUFnGP1OcFnqfbH8Rzsr-YbiyQOg5d");
#curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
#curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
#curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
#curl_exec($curl);

?>