<?php
chdir('/home/web/dev-web/badblock-website');
shell_exec('git pull http://websyncbot:JTXg4hc7LxMUc8ERsjEC4GhUMMWCRhBWSeALBq6FjxbakM5Vwvzv3dzJgFm32mS7@lusitania.badblock.fr/Website/badblock-website.git');
shell_exec('php composer.phar install');
shell_exec('php composer.phar update');
