<?php

if(__FILE__ == $_SERVER['REQUEST_URI'])
    return header('Location: '.$_SERVER['HTTP_REFERER']);

return [



];