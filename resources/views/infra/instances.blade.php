<div class="col-xl-3 col-md-6">
    <div class="card-box">
        <h4 class="header-title mt-0 m-b-30">Groupe : {{ $k }}</h4>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>#</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            @foreach($server as $srv)
                <tr>
                    <th scope="row">
                        {{ $k }}_{{ $srv->id }}
                        @if($srv->gameState == "WAITING")
                            <span class="badge badge-warning">Waiting...</span>
                        @elseif($srv->gameState == "RUNNING")
                            <span class="badge badge-success">Running</span>
                        @else
                            <span class="badge badge-danger">Offline</span>
                        @endif
                    </th>
                    <td>
                        <a href="/" class="btn btn-icon btn-warning"> <i class="fa fa-wrench"></i> </a>
                    </td>
                </tr>
            @endforeach
            </tbody>
        </table>

    </div>
</div>