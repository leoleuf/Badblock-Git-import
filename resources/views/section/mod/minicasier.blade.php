<link href="/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/core.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/components.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/icons.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/pages.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/menu.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/responsive.css" rel="stylesheet" type="text/css" />
<div class="col-lg-12">
    <div class="card-box">
        <h4 class="m-t-0 header-title">100 dernières sanctions de {{ $pseudo }}</h4>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>#</th>
                <th>Type</th>
                <th>Banner</th>
                <th>Fin</th>
                <th>Raison</th>
            </tr>
            </thead>
            <tbody id="sanction_list">
            @foreach($sanction as $row)
                <tr>
                    <td>{{ $row->id }}</td>
                    <td>{{ $row->type }}</td>
                    <td>{{ $row->banner }}</td>
                    <td>
                        @if($row->expire == -1)
                            Infinis
                        @else
                            {{ date('d-m-Y', $row->expire / 1000) }}
                        @endif
                    </td>
                    <td>{{ $row->reason }}</td>
                </tr>
            @endforeach
            </tbody>
        </table>
    </div>
</div>
