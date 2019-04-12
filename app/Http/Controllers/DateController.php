<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 10/04/2019
 * Time: 11:37
 */

namespace App\Http\Controllers;


class DateController extends Controller
{

    public static function formatDate($date)
    {
        $date = date_create_from_format('Y-m-d H:m:s', $date);
        date_add($date, date_interval_create_from_date_string('2 hours'));
        return date_format($date, 'd/m/Y à H:m:s');
    }

    public static function formatDateWithoutTime($date)
    {
        $date = date_create_from_format('Y-m-d', $date);
        return date_format($date, 'd/m/Y');
    }

    public static function formatDateString($date)
    {
        $date = str_replace('-', '/', $date);
        $date = str_replace(' ', ' à ', $date);
        return $date;
    }

}