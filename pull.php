<?php
chdir('/home/web/dev-web/badblock-website');
shell_exec('git pull http://web_bot:kPnZSY3DW9gCCnyBxrA2Fm6eAa3tcafe@vps446463.ovh.net/Website/badblock-website.git');
shell_exec('composer install');
shell_exec('composer update');