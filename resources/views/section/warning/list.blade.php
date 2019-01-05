@extends('layouts.app')

@section('content')
<div class="row">
  <div class="col-12">
    <div class="card-box">
      <h4 class="m-t-0 header-title">Liste des avertissements</h4>
      <div class="row">
        <div class="col-12">
          <div class="p-20">
            <table class="table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Pseudo</th>
                  <th>Titre</th>
                  <th>Date</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                @foreach (DB::table('warning')->get() as $key => $data)
                  <tr>
                    <th scope="row">{{ $key + 1 }}</th>
                    <td>{{ $data->pseudo }}</td>
                    <td>{{ $data->title }}</td>
                    <td>{{ $data->created_at}}</td>
                    <td><a href="/section/avertissement/{{ $data->id }}">En savoir plus</a></td>
                  </tr>
                @endforeach
              </tbody>
            </table>
          </div>
        </div>

      </div>
      <!-- end row -->

    </div> <!-- end card-box -->
  </div><!-- end col -->
</div>
@endsection
