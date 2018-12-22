<?php

namespace App\Http\Controllers;

use Image;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\Storage;



class PanelController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        if(Auth::user()->is_admin == 1){
            $server = DB::select('select * from server_list');
        }else{
            //Récupération des serveur de l'utilisateur
            $server = DB::select('select * from server_list where user_id = ?', [Auth::user()->id]);
        }


        return view('panel.index',['server' => $server]);
    }

    public function adminvalidate(Request $request, $id)
    {
        if (Auth::user()->is_admin != 1) {
            return redirect('/dashboard')->withInput();
        }

        $id = intval($id);
        $server = DB::select('select * from server_list where id = ?', [$id]);

        if ($server == null || count($server) == 0)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Serveur inconnu.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/admin')->withInput();
        }

        $server = $server[0];

        if ($server->actived == 1)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Serveur déjà validé.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/admin')->withInput();
        }

        DB::table('server_list')
            ->where('id', '=', $id)
            ->update(
                [
                    'actived' => 1,
                ]
            );

        $request->session()->flash('flash', [
            array(
                'level' => 'success',
                'message' => 'Serveur validé avec succès.',
                'important' => true
            )
        ]);
        return redirect('/dashboard/admin')->withInput();
    }

    public function adming()
    {
        if(Auth::user()->is_admin != 1) {
            return redirect('/dashboard')->withInput();
        }

        $about = json_decode(Redis::get('about'));

        $server = DB::select('select * from server_list WHERE actived = 0 ORDER BY id DESC;');

        $data = [];
        $hour = -1;
        while($hour++ < 24)
        {
            $time1 = date('Y-m-d H:i:s',mktime($hour,0,0));
            $time2 = date('Y-m-d H:i:s',mktime($hour + 1,0,0));

            $count = DB::table('vote_logs')
                ->where('date', '>=', $time1)
                ->where('date', '<=', $time2)
                ->count();

            $count2 = DB::table('click_logs')
                ->where('date', '>=', $time1)
                ->where('date', '<=', $time2)
                ->count();

            $count3 = DB::table('copy_logs')
                ->where('date', '>=', $time1)
                ->where('date', '<=', $time2)
                ->count();

            array_push($data, [date('H',mktime($hour,0,0)), $count, $count2, $count3]);
        }

        $datam = [];
        $start = strtotime('-80 days');
        $end = strtotime("midnight tomorrow");

        while($end > $start)
        {
            $time1 = date('Y-m-d H:i:s',$start);

            $time2 = date('Y-m-d H:i:s',$start + 86400);

            $count = DB::table('vote_logs')
                ->where('date', '>=', $time1)
                ->where('date', '<=', $time2)
                ->count();

            $count2 = DB::table('click_logs')
                ->where('date', '>=', $time1)
                ->where('date', '<=', $time2)
                ->count();

            $count3 = DB::table('copy_logs')
                ->where('date', '>=', $time1)
                ->where('date', '<=', $time2)
                ->count();

            array_push($datam, [$time1,$time2, $count, $count2, $count3]);

            $start = $start + 86400;
        }

        return view('panel.admin', ['about' => $about, 'server' => $server, 'data' => $data, 'datam' => $datam]);
    }

    public function addServer()
    {
        $tagar = array();
        foreach (config('tag.tag') as $ko => $vo)
        {
            $tagar = array_merge($tagar, $vo);
        }

        $tagar = array_map('strtolower', $tagar);

        return view('panel.addserver', ['tag' => $tagar]);
    }


    public function addServerSave(Request $request)
    {

        try {
            if (isset($_POST['category'])) {

                if (!in_array($_POST['category'], config('tag.cat'))) {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Catégorie inconnue. Veuillez sélectionner une catégorie valide.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/add-server')->withInput();
                }

                if (isset($_POST['tags']) && !empty($_POST['tags'])) {
                    $_POST['tags'] = strtolower($_POST['tags']);
                    if (json_decode("[" . $_POST['tags'] . "]") == null) {
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Impossible de décoder la liste des tags, veuillez séparer les tags par une virgule.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/add-server')->withInput();
                    }

                    $tag = array_values(config('tag.tag')[$_POST['category']]);
                    $tags = json_decode("[" . $_POST['tags'] . "]");


                    foreach ($tags as $k => $v) {
                        if (!in_array($v, $tag)) {
                            $request->session()->flash('flash', [
                                array(
                                    'level' => 'danger',
                                    'message' => 'Tag inconnu pour la catégorie ' . htmlspecialchars($_POST['category']) . ' : ' . htmlspecialchars($v) . '. Restez cohérent sur les tags.',
                                    'important' => true
                                )
                            ]);
                            return redirect('/dashboard/add-server')->withInput();
                        }
                    }
                } else {
                    $tags = array();
                }

            } else {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez choisir une catégorie valide.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            $input = $request->all();

            $p = null;

            if (isset($_POST['group4'])) {
                $p = strtoupper(htmlspecialchars($_POST['group4']));
                if ($p != "TRUE" && $p != "JSON" && $p != "CALLBACK" && $p != "VOTIFIER") {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Veuillez sélectionner une méthode de vérification des votes valide.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/add-server')->withInput();
                }
            } else {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez sélectionner une méthode de vérification des votes.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            if (!isset($input['name']) OR empty($input['name'])) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez saisir un nom de serveur.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            if (strlen($input['name']) < 4) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Le nom du serveur doit contenir au minimum 4 caractères.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            if (!isset($input['text']) OR strlen($input['text']) < 500) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez fournir une description courte UNIQUE pour l\'affichage sur le classement d\'au moins 500 caractères.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            if (!isset($input['desc']) OR strlen($input['desc']) < 1000) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez fournir une description complète UNIQUE d\'au moins 1000 caractères pour l\'affichage sur le classement.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            $cbk = "";
            if ($p != null && $p == "CALLBACK" && (!isset($input['callback_url']) OR empty($input['callback_url']))) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Vous avez sélectionné la méthode CALLBACK. Veuillez saisir une URL CallBack valide.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            if ($p != null && $p == "VOTIFIER") {
                if (!isset($input['votifier_servername']) OR empty($input['votifier_servername']) OR
                    !isset($input['votifier_serverip']) OR empty($input['votifier_serverip']) OR
                    !isset($input['votifier_serverport']) OR empty($input['votifier_serverport']) OR
                    !isset($input['votifier_publickey']) OR empty($input['votifier_publickey'])) {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Vous avez sélectionné la méthode VOTIFIER. Veuillez saisir le nom du serveur, l\'IP, le port et la clé publique Votifier.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/add-server')->withInput();
                }
            }

            if ($p != null && $p == "CALLBACK") {
                if (!filter_var($input['callback_url'], FILTER_VALIDATE_URL)) {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'URL de CallBack invalide.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/add-server')->withInput();
                }

                $cbk = htmlspecialchars($input['callback_url']);
            }

            if ($request->file('image') == null) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez fournir une image valide pour l\'affichage de votre serveur sur le classement.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            $count = DB::table('server_list')
                ->where('name', '=', encname($input['name']))
                ->count();

            if ($count > 0) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Le nom de ce serveur existe déjà sur notre classement. Veuillez saisir un autre nom.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            $adress = !isset($input['adress']) ? '' : $input['adress'];
            $website = !isset($input['website']) ? '' : $input['website'];

            $votifierArray = array();

            if ($p == "VOTIFIER" && isset($input['votifier_servername']) && count($input['votifier_servername']) > 0) {
                foreach ($input['votifier_servername'] as $k => $row) {

                    if (strlen($k) == 0 || !is_numeric($k)) {
                        continue;
                    }

                    if (!isset($input['votifier_serverip'][$k])) {
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Vous avez oublié d\'entrer l\'IP du Serveur Votifier #' + $k . '.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/add-server')->withInput();
                    }

                    if (!isset($input['votifier_serverport'][$k])) {
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Vous avez oublié d\'entrer le port du Serveur Votifier #' + $k . '.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/add-server')->withInput();
                    }

                    if (!isset($input['votifier_publickey'][$k])) {
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Vous avez oublié d\'entrer la clé publique du Serveur Votifier #' + $k . '.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/add-server')->withInput();
                    }

                    $votifierArray[$k] = array(
                        'name' => $row,
                        'ip' => $input['votifier_serverip'][$k],
                        'port' => $input['votifier_serverport'][$k],
                        'key' => $input['votifier_publickey'][$k]
                    );
                }
            }

            $votifierArray = json_encode($votifierArray);

            $id = DB::table('server_list')->insertGetId(
                [
                    'name' => $input['name'],
                    'user_id' => Auth::user()->id,
                    'description' => $input['desc'],
                    'short_desc' => $input['text'],
                    'ip' => $adress,
                    'website' => $website,
                    'callback_url' => $cbk,
                    'votetype' => $p,
                    'cat' => $input['category'],
                    'api_key' => $this->generateRandomString(),
                    'tag' => json_encode($tags),
                    'created_at' => date("Y-m-d H:i:s"),
                    'actived' => false,
                    'votifierdata' => $votifierArray
                ]
            );

            DB::table('logs')->insert(
                [
                    'user_id' => Auth::user()->id,
                    'action' => 'Ajout du serveur ' . $input['name'],
                    'date' => date("Y-m-d H:i:s"),
                    'ip' => $_SERVER['REMOTE_ADDR'],
                    'success' => true
                ]
            );

            try {
                if ($request->file('image') != null) {

                    $f = @$request->file('image');

                    if ($f == null)
                    {
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Veuillez mettre un logo.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/add-server')->withInput();
                    }

                    $path = @$f->storeAs(
                        'public/icone', "icon" . $id . ".jpg"
                    );

                    $slider = @Image::make($f->getRealPath());

                    if ($slider == null)
                    {
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Veuillez mettre un logo valide.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/add-server')->withInput();
                    }

                    $slider->fit(190, 190);

                    $v = public_path('storage/icone/icon' . $id . '.jpg');

                    $slider->save($v);

                }
            } catch (Exception $e) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez mettre un logo.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }

            try {
                if ($request->file('banner') != null) {

                    $f = $request->file('banner');
                    $path = $f->storeAs(
                        'public/banner', "banner" . $id . ".jpg"
                    );

                    $slider = Image::make($f->getRealPath());

                    $slider->fit(1903, 448);

                    $v = public_path('storage/banner/banner' . $id . '.jpg');

                    $slider->save($v);

                }
            } catch (Exception $e) {

            }

            return redirect("/dashboard");
        }
        catch (Exception $e)
        {
            exit($e);
            return;
        }
    }

   /* public function delServer($id){
        $server = DB::select('select user_id from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id == Auth::user()->id){
            DB::table('logs')->insert(
                [
                    'user_id' => Auth::user()->id,
                    'action' => 'Supression du serveur n°' . $id,
                    'date' => date("Y-m-d H:i:s"),
                    'ip' => $_SERVER['REMOTE_ADDR'],
                    'success' => true
                ]
            );

            DB::table('server_list')->where('id', '=', $id)->delete();
        }

        return redirect("/dashboard");
    }*/

    public function votes($id){
        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id == Auth::user()->id){
            $votes = DB::select('select * from vote_logs where server_id = ? order by id desc LIMIT 50;', [$id]);

            return view('panel.votes', ['data' => $server[0], 'votes' => $votes]);
        }else{
            return redirect("/dashboard");
        }
    }

    public function editServer($id){
        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id == Auth::user()->id){
            $tagar = array();
            foreach (config('tag.tag') as $ko => $vo)
            {
                $tagar = array_merge($tagar, $vo);
            }

            $tagar = array_map('strtolower', $tagar);
            return view('panel.editserver', ['data' => $server[0],'tag' => $tagar]);
        }else{
            return redirect("/dashboard");
        }
    }

    public function editServerSave(Request $request, $id)
    {
        $id = intval($id);

        $server = DB::select('select * from server_list where id = ? LIMIT 1', [$id]);

        if (empty($server))
        {
            return redirect('/dashboard');
        }

        if ($server[0]->user_id != Auth::user()->id)
        {
            return redirect('/dashboard');
        }

        $p = null;

        if (isset($_POST['group4']))
        {
            $p = strtoupper(htmlspecialchars($_POST['group4']));
            if ($p != "TRUE" && $p != "JSON" && $p != "CALLBACK" && $p != "VOTIFIER")
            {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Veuillez sélectionner une méthode de vérification des votes valide.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/edit-server/'.$id)->withInput();
            }
        }
        else
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez sélectionner une méthode de vérification des votes.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/edit-server/'.$id)->withInput();
        }

        $adblock = 0;
        if (isset($_POST['adblock']))
        {
            $adblock = intval($_POST['adblock']);
        }

        if ($p != null && $p == "VOTIFIER") {
            if (!isset($_POST['votifier_servername']) OR empty($_POST['votifier_servername']) OR
                !isset($_POST['votifier_serverip']) OR empty($_POST['votifier_serverip']) OR
                !isset($_POST['votifier_serverport']) OR empty($_POST['votifier_serverport']) OR
                !isset($_POST['votifier_publickey']) OR empty($_POST['votifier_publickey'])) {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'Vous avez sélectionné la méthode VOTIFIER. Veuillez saisir le nom du serveur, l\'IP, le port et la clé publique Votifier.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/add-server')->withInput();
            }
        }

            if (isset($_POST['tags']) && !empty($_POST['tags']))
            {
                $_POST['tags'] = strtolower($_POST['tags']);
                if (json_decode("[" . $_POST['tags'] . "]") == null)
                {
                    $request->session()->flash('flash', [
                        array(
                            'level' => 'danger',
                            'message' => 'Impossible de décoder la liste des tags, veuillez séparer les tags par une virgule.',
                            'important' => true
                        )
                    ]);
                    return redirect('/dashboard/edit-server/'.$id)->withInput();
                }

                $tag = array_values(config('tag.tag')[$server[0]->cat]);
                $tags = json_decode("[" . $_POST['tags'] . "]");


                foreach ($tags as $k => $v){
                    if (!in_array($v, $tag)){
                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Tag inconnu pour la catégorie '.htmlspecialchars($server[0]->cat).' : '.htmlspecialchars($v).'. Restez cohérent sur les tags.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/edit-server/'.$id)->withInput();
                    }
                }
            }
            else
            {
                $tags = array();
            }
        $input = $request->all();

        if (!isset($input['name']) OR empty($input['name']))
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez saisir un nom de serveur.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/edit-server/'.$id)->withInput();
        }

        $cbk = $server[0]->callback_url;
        if (isset($input['callback_url']) && !empty($input['callback_url']))
        {
            if (!filter_var($input['callback_url'], FILTER_VALIDATE_URL))
            {
                $request->session()->flash('flash', [
                    array(
                        'level' => 'danger',
                        'message' => 'URL de CallBack invalide.',
                        'important' => true
                    )
                ]);
                return redirect('/dashboard/edit-server/'.$id)->withInput();
            }

            $cbk = htmlspecialchars($input['callback_url']);
        }

        if (strlen($input['name']) < 4)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Le nom du serveur doit contenir au minimum 4 caractères.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/edit-server/'.$id)->withInput();
        }

        if (!isset($input['text']) OR strlen($input['text']) < 500)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez fournir une description courte UNIQUE pour l\'affichage sur le classement d\'au moins 500 caractères.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/edit-server/'.$id)->withInput();
        }

        if (!isset($input['desc']) OR strlen($input['desc']) < 1000)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Veuillez fournir une description complète d\'au moins 1000 caractères pour l\'affichage sur le classement.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/edit-server/'.$id)->withInput();
        }

        $count = DB::table('server_list')
            ->where('id', '!=', encname($server[0]->id))
            ->where('name', '=', encname($input['name']))
            ->count();

        if ($count > 0)
        {
            $request->session()->flash('flash', [
                array(
                    'level' => 'danger',
                    'message' => 'Le nom de ce serveur existe déjà sur notre classement. Veuillez saisir un autre nom.',
                    'important' => true
                )
            ]);
            return redirect('/dashboard/edit-server/'.$id)->withInput();
        }

        $adress = !isset($input['adress']) ? '' : $input['adress'];
        $website = !isset($input['website']) ? '' : $input['website'];

        if ($request->file('image') != null) {

            $f = $request->file('image');
            $path = $f->storeAs(
                'public/icone', "icon" . $id . ".jpg"
            );

            $slider = Image::make($f->getRealPath());

            $slider->fit(190, 190);

            $v = public_path('storage/icone/icon' . $id . '.jpg');

            $slider->save($v);

        }

        if ($request->file('banner') != null) {

            $f = $request->file('banner');
            $path = $f->storeAs(
                'public/banner', "banner" . $id . ".jpg"
            );

            $slider = Image::make($f->getRealPath());

            $slider->fit(1903, 448);

            $v = public_path('storage/banner/banner' . $id . '.jpg');

            $slider->save($v);

        }

        $votifierArray = array();


        if (isset($input['votifier_servername']))
        {
            foreach ($input['votifier_servername'] as $k => $row)
            {
                if (strlen($k) > 0 && strlen($row) > 0) {
                    if (!isset($input['votifier_serverip'][$k])) {

                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Vous avez oublié d\'entrer l\'IP du Serveur Votifier #'.$row.'.',
                                'important' => true
                            )
                        ]);

                        return redirect('/dashboard/edit-server/'.$id)->withInput();
                    }

                    if (!isset($input['votifier_serverport'][$k])) {

                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Vous avez oublié d\'entrer le port du Serveur Votifier #'.$row.'.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/edit-server/'.$id)->withInput();

                    }

                    if (!isset($input['votifier_publickey'][$k])) {

                        $request->session()->flash('flash', [
                            array(
                                'level' => 'danger',
                                'message' => 'Vous avez oublié d\'entrer la clé publique du Serveur Votifier #'.$row.'.',
                                'important' => true
                            )
                        ]);
                        return redirect('/dashboard/edit-server/'.$id)->withInput();

                    }

                    $votifierArray[] = array(
                        'name' => $row,
                        'ip' => $input['votifier_serverip'][$k],
                        'port' => $input['votifier_serverport'][$k],
                        'key' => $input['votifier_publickey'][$k]
                    );
                }
            }
        }

        $votifierArray = json_encode($votifierArray);

        DB::table('server_list')
            ->where('id', '=', $id)
            ->update(
                [
                    'name' => $input['name'],
                    'user_id' => Auth::user()->id,
                    'description' => $input['desc'],
                    'short_desc' => $input['text'],
                    'ip' => $adress,
                    'website' => $website,
                    'adblock' => $adblock,
                    'callback_url' => $cbk,
                    'created_at' => date("Y-m-d H:i:s"),
                    'tag' => json_encode($tags),
                    'votetype' => $p,
                    'votifierdata' => $votifierArray
                ]
            );

        DB::table('logs')->insert(
            [
                'user_id' => Auth::user()->id,
                'action' => 'Edition du serveur n°' . $id,
                'date' => date("Y-m-d H:i:s"),
                'ip' => $_SERVER['REMOTE_ADDR'],
                'success' => true
            ]
        );

        return redirect('/dashboard')->withInput();

    }

    public function generateRandomString() {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < 30; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }





}
