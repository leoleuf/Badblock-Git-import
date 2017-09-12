<?php
chdir('/home/web/dev-web/badblock-website/public');
shell_exec('mv index.php _index.php');
shell_exec('mv maintenance.php index.php');