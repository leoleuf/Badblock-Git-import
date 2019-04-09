@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="panel-body">
                    <div class="card-box">
                        <h4 class="m-t-0 header-title">Ajouter une preuve.</h4>
                        <p class="text-muted font-14 m-b-20">
                            N'oubliez pas de rentrer votre TOKEN d'identification.
                        </p>
                        @if ($message = Session::get('status'))

                            <img src="https://cdn.badblock.fr/upload/{{ Session::get('img') }}" class="img-fluid">

                        @endif
                        @if ($message = Session::get('failed'))

                            <p class="text-danger">{{ $message }}</p>

                        @endif
                        <div class="row">
                            <div class="col-12 col-md-6">
                                <form action="/hookix/upload" method="post" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <input type="text" class="form-control" placeholder="TOKEN" name="hookixToken">
                                    </div>
                                    <div class="form-group">
                                        <label for="exampleInputFile">File input</label>
                                        <input type="file" name="profile_image" id="exampleInputFile" multiple />
                                    </div>
                                    {{ csrf_field() }}
                                    <button type="submit" class="btn btn-success">Uploader</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
