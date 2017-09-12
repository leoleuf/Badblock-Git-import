<?php
chdir('/home/web/dev-web/badblock-website/public');
shell_exec('mv index.php maintenance.php');
shell_exec('mv _index.php index.php');