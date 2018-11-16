<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Redis;
use Illuminate\Session\TokenMismatchException;
use Illuminate\Support\Facades\DB;
use Session;
use Auth;
use Illuminate\Http\Request;

class ContactController extends Controller
{


    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function contact()
    {
        return view('front.contact');
    }

    public function contactPost(Request $request)
    {
        if (!isset($_POST['name']) OR empty($_POST['name']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez fournir un nom complet.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (!isset($_POST['email']) OR empty($_POST['email']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez fournir une adresse e-mail.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (!isset($_POST['subject']) OR empty($_POST['subject']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez fournir un sujet.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (!isset($_POST['message']) OR empty($_POST['message']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez fournir un message.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (!isset($_POST['g-recaptcha-response']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez compléter le captcha de sécurité.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (empty($_POST['g-recaptcha-response']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez compléter le captcha de sécurité.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        $check = $this->check($_POST['g-recaptcha-response']);

        $check = json_decode($check);

        if ($check == null || $check == false)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez compléter le captcha de sécurité.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (!isset($check->success))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez compléter le captcha de sécurité.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        if (!$check->success)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez compléter le captcha de sécurité.',
                    'important' => true
                )
            ]);
            return redirect('/contact')->withInput();
        }

        $mail = new \App\Mail(true);
        $mail->sendMail('serveur.multigames.net@gmail.com', "[Serveur MultiGames] ".htmlspecialchars($_POST['subject']),
            '
            Nom complet : '.htmlspecialchars($_POST['name']).'<br />
            Adresse e-mail : '.htmlspecialchars($_POST['email']).'<br />
            Sujet : '.htmlspecialchars($_POST['subject']).'<br /><br />
            Message : '.htmlspecialchars($_POST['message']));

        $request->session()->flash('flash', [
            array(
                'level' => 'success',
                'message' => 'Votre message a bien été envoyé.',
                'important' => true
            )
        ]);

        return view('front.contact');
    }

    function check($input)
    {
        $data = [
            'secret' => "6Lf8amQUAAAAAB0k1eHNeM6t8OlIVmdN0KVYzqXH",
            'response' => $input,
            'remoteip' => $_SERVER['REMOTE_ADDR']
        ];

        $curl = curl_init("https://www.google.com/recaptcha/api/siteverify");
        curl_setopt($curl, CURLOPT_POST, true);
        curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        $response = curl_exec($curl);
        curl_close($curl);
        return $response;
    }

}