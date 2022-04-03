curl http://localhost:56677/creditsm/_doc/723e3a58-c512-4e70-8825-775ce2e32754 | json_pp

curl http://localhost:56677/creditsm/_search | less

curl http://localhost:56677/_cat/indices\?v

curl -X POST "http://localhost:56677/creditsm/_search?pretty" -H 'Content-Type: application/json' -d' {
    "query": {
        "match": {
            "state" : "CREDIT_REJECTED"
        } 
    }
    "_source": false }'
