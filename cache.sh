rm -r /var/cache/ngx*
rm -r /var/cache/nginx
php artisan cache:clear
php artisan route:cache
php artisan view:clear
php artisan config:cache
php artisan optimize
service nginx restart
