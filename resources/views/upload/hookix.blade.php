@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="panel-body">
                    <div class="card-box">
                        <h4 class="m-t-0 header-title">Ajouter une preuve.</h4>
                        @if ($message = Session::get('status'))

                            <p>{!! $message !!}</p>
                        @endif
                        @if ($message = Session::get('failed'))

                            <p class="text-danger">{{ $message }}</p>

                        @endif
                        <div class="row">
                            <div class="col-12 col-md-6">
                                <form action="/hookix/upload" method="post" enctype="multipart/form-data">
                                    <div class="form-group">
                                        <label for="exampleInputFile">File input</label>
                                        <input type="file" class="form-control" name="screen[]" multiple />
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
