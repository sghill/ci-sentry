function(head, req) {
    function pp(x) {
        send(x.join(',') + '\n')
    }
    start({
        "headers": {
            "Content-Type": "text/csv",
            "Content-Disposition": "attachment; filename='build-report.csv'"
        }
    });
    pp(['name','run','durationInMillis','completedAt','result']);
    var row;
    while(row = getRow()) {
        var v = row.value;
        pp([v.name, v.run, v.durationInMillis, v.completedAt, v.result]);
    }
}
