<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Redis;
use Illuminate\Session\TokenMismatchException;
use Illuminate\Support\Facades\DB;
use Session;
use Auth;

class HomeController extends Controller
{


    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $about = json_decode(Redis::get('about'));

        return view('front.accueil', ['about' => $about]);
    }

    public function partenaires2()
    {
        $data = DB::table('server_list')
            ->where('votes', '=', 0)
            ->get();

        $data = $data->toArray();

        $mu = "";
        foreach ($data as $p)
        {
            $dq = DB::table('users')
                ->where('id', '=', $p->user_id)
                ->get();

            $dq = $dq->toArray()[0];
            $mu = $mu.$dq->email.PHP_EOL;
        }

        return view('front.partenaires2', ['mu' => '']);
    }

    public function lien()
    {
        return view('front.lien');
    }

    public function partenaires()
    {
        return view('front.partenaires');
    }

    public function pub()
    {
        return view('front.mise-en-avant');
    }

    public function assurance()
    {
        return view('front.assurance');
    }

    public function searchmethod()
    {
        if (!isset($_POST['serveur']))
        {
            abort(404);
        }

        $term = trim(htmlspecialchars($_POST['serveur']));
        if (strlen($term) < 1)
        {
            abort(404);
        }

        return redirect('/recherche/'.encname($term), 301);
    }

    public function search($term)
    {

        $internalTerm = encname($term);
        $searchTerm = ucfirst(str_replace("-", " ", $term));

        $data = DB::table('server_list')
            ->where('name', 'LIKE', '%'.encname($searchTerm).'%')
            ->orWhere('description', 'LIKE', '%'.encname($searchTerm).'%')
            ->orWhere('short_desc', 'LIKE', '%'.encname($searchTerm).'%')
            ->orWhere('ip', 'LIKE', '%'.encname($searchTerm).'%')
            ->orWhere('website', 'LIKE', '%'.encname($searchTerm).'%')
            ->orWhere('tag', 'LIKE', '%'.enctag($searchTerm).'%')
            ->orWhere('cat', 'LIKE', '%'.encname($searchTerm).'%')
            ->get();

        $data = $data->toArray();

        return view('front.search', ['internalTerm' => $internalTerm, 'searchTerm' => $searchTerm, 'data' => $data]);
    }

    public function isABot() {
        return (
            isset($_SERVER['HTTP_USER_AGENT'])
            && preg_match('/bot|crawl|slurp|spider|mediapartners/i', $_SERVER['HTTP_USER_AGENT'])
        );
    }

    protected function seobot() {

        return (
            isset($_SERVER['HTTP_USER_AGENT'])
            && preg_match('/ahref|mj12bot|semrush/i', $_SERVER['HTTP_USER_AGENT'])
        );
    }

    public function category($cat)
    {

        $catName = strtolower($cat);
        $l = array();
        foreach (config('tag.cat') as $k)
        {
            $l[enctag($k)] = 0;
        }

        $l = array_keys($l);

        // probleme de certains backlinks
        if ($catName == "minecraf")
        {
            return redirect('/minecraft', 301);
        }

        if (!in_array($catName, $l))
        {
            abort(404);
        }

        $nb = Redis::get('page:'.$catName.':number');

        $data = json_decode(Redis::get('page:'.$catName.':data:1'));

        if ($data == null)
        {
            return view('front.nowebsite', ['catName' => $catName, 'current_page' => 1]);
        }

        $addon = '';
        if ($this->seobot()) {
            foreach ($data as $srv) {
                if (isset($srv->website) && !empty($srv->website) && $srv->actived == 1)
                {
                    $addon .= '<a title="'.htmlspecialchars(htmlentities($srv->name)).'" href="'.htmlspecialchars(htmlentities($srv->website)).'">'.htmlspecialchars(htmlentities($srv->name)).'</a><br />';
                }
            }
        }

        $tagsInfo = json_decode(Redis::get('tags:'.$catName));
        $tagsInfo = (array) $tagsInfo;
        arsort($tagsInfo);

        $tagsInfo = array_slice($tagsInfo, 0, 27);

        $votelistok = false;
        if (Session::exists('votelistok')) {
            Session::remove('votelistok');
            $votelistok = true;
        }

        $about = json_decode(Redis::get('about'));

        if ($this->isABot())
        {
            return view('front.category', ['addon' => $addon, 'about' => $about, 'bot' => true, 'votelistok' => $votelistok, 'tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'current_page' => 1 ,'page_number' => $nb, 'lnk' => '']);
        }

        return view('front.category', ['addon' => $addon, 'about' => $about, 'votelistok' => $votelistok, 'tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'current_page' => 1 ,'page_number' => $nb, 'lnk' => '']);
    }

    public function faq()
    {
        return view('front.faq');
    }

    public function installtrue()
    {
        return view('front.installtrue');
    }

    public function installjson()
    {
        return view('front.installjson');
    }

    public function installvotifier()
    {
        return view('front.installvotifier');
    }

    public function installcallback()
    {
        return view('front.installcallback');
    }

    public function addserver()
    {
        return view('front.add-server');
    }

    public function review()
    {
        return view('front.review');
    }

    public function rules()
    {
        return view('front.rules');
    }

    public function discover()
    {
        return view('front.discover');
    }

    public function tos()
    {
        return view('front.tos');
    }

    public function aboutus()
    {
        $about = json_decode(Redis::get('about'));

        return view('front.aboutus', ['about' => $about]);
    }

    public function logout($key)
    {
        if (Session::token() != $key)
        {
            throw new TokenMismatchException;
        }

        Auth::logout();
        return redirect('/', 301);
    }

    public function api()
    {
        return view('front.api');
    }

    public function page($cat, $id)
    {
        if ($id < 2)
        {
            return redirect("/".encname($cat), 301);
        }

        $catName = strtolower($cat);
        $l = array();
        foreach (config('tag.cat') as $k)
        {
            $l[enctag($k)] = 0;
        }

        $l = array_keys($l);

        if (!in_array($catName, $l))
        {
            abort(404);
        }

        $nb = Redis::get('page:'.$catName.':number');

        if ($id < ($nb +1)){
            $data = json_decode(Redis::get('page:'.$catName.':data:'.$id));

            $tagsInfo = json_decode(Redis::get('tags:'.$catName));
            $tagsInfo = (array) $tagsInfo;
            arsort($tagsInfo);

            $tagsInfo = array_slice($tagsInfo, 0, 5);

            $about = json_decode(Redis::get('about'));
            return view('front.category', ['about' => $about, 'tags' => $tagsInfo, 'catName' => $catName, 'data' => $data, 'current_page' => $id ,'page_number' => $nb]);
        }else{
            return redirect("/".$catName, 301);
        }

    }

    public function tag($cat, $id, $page = 1)
    {
        $catName = strtolower($cat);
        $l = array();
        foreach (config('tag.cat') as $k)
        {
            $l[encname($k)] = 0;
        }

        $l = array_keys($l);
        
        if (!in_array($catName, $l))
        {
            abort(404);
        }

        $shownTag = null;
        $tags = array();
        foreach (config('tag.tag')[seocat($catName)] as $k)
        {
            if (enctag($k) == $id)
            {
                $shownTag = $k;
            }
            $tags[enctag($k)] = 0;
        }


        if (!isset($tags[$id])) {
            abort(404);
        }

        $data = json_decode(Redis::get('tag:'.$catName.':' . $id . ":" . $page));
        if ($data == null) {
            abort(404);
        }

        $nb = Redis::get('page:number');
        $about = json_decode(Redis::get('about'));

        $tagsInfo = json_decode(Redis::get('tags:'.$catName));
        $tagsInfo = (array) $tagsInfo;
        arsort($tagsInfo);

        $tagsInfo = array_slice($tagsInfo, 0, 5);

        // show good tag name
        if ($shownTag != null)
        {
            $id = $shownTag;
        }
        
        return view('front.category', ['tags' => $tagsInfo, 'tag' => $id, 'catName' => $catName, 'data' => $data, 'current_page' => $page ,'page_number' => $nb, 'about' => $about]);
    }

}
