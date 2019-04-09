@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="panel-body">
                    <div class="card-box">
                        <h4 class="m-t-0 header-title">Ajouter des points boutique.</h4>
                        <p class="text-muted font-14 m-b-20">
                            Attention à ne pas entrer de valeur négative.
                        </p>
                        @if ($message = Session::get('status'))

                            <img src="https://cdn.badblock.fr/upload/{{ Session::get('img') }}" class="img-fluid">

                        @endif
                        <form action="/hookix/upload" method="post" enctype="multipart/form-data">
                            <div class="form-group">
                                <label for="exampleInputFile">File input</label>
                                <input type="file" name="profile_image" id="exampleInputFile" multiple />
                            </div>
                            {{ csrf_field() }}
                            <button type="submit" class="btn btn-default">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
