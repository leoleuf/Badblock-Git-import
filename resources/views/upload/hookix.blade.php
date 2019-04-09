@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
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
@endsection
