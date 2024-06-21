# transfer-service


# Transfer Fund

curl --location 'http://localhost:8080/api/v1/transactions/transfer-fund' \
--header 'Content-Type: application/json' \
--data '{
"transactionReference": "883939393939",
"amount": 10,
"description": "Test Transfer",
"beneficiaryBankCode": "044",
"beneficiaryBankName": "First Bank",
"beneficiaryAccountName": "Dele Sam",
"beneficiaryAccountNumber": "78828882828",
"originatorBankCode": "053",
"originatorBankName": "GTBank",
"originatorAccountName": "Yusuf Monday",
"originatorAccountNumber": "20222020202"
}'

response:
{
"message": "Request Successful",
"data": {
"id": 202,
"transactionReference": "99900993939",
"amount": 10.00,
"transactionFee": null,
"billedAmount": null,
"status": "SUCCESSFUL",
"statusMessage": "Request Successful",
"description": "Test Transfer",
"hasCommission": false,
"commission": null,
"originatorAccountNumber": "20222020202",
"originatorAccountName": "Yusuf Monday",
"originatorBankCode": "053",
"originatorBankName": "GTBank",
"beneficiaryAccountNumber": "78828882828",
"beneficiaryAccountName": "Dele Sam",
"beneficiaryBankCode": "044",
"beneficiaryBankName": "First Bank",
"createdAt": "2024-06-21T16:12:38.785771",
"updatedAt": "2024-06-21T16:12:38.78587"
},
"responseCode": "00"
}



# Search Transactions
note: all search params are optional

curl --location 'http://localhost:8080/api/v1/transactions/search?transactionReference=&originatorAccountNumber=&beneficiaryAccountNumber=&status=FAILED&startDate=2024-06-19&endDate=2024-06-21&page=0&size=25&sort=id%2Cdesc' \
--data ''

response:

{
"message": "Request Successful",
"data": {
"totalPages": 1,
"totalElements": 10,
"size": 25,
"content": [
{
"id": 1,
"transactionReference": "UIOUU182992828",
"amount": 20000.00,
"transactionFee": 100.00,
"billedAmount": 19900.00,
"status": "SUCCESSFUL",
"statusMessage": "Request Successful",
"description": "Test Transfer",
"hasCommission": false,
"commission": 20.00,
"originatorAccountNumber": "20222020202",
"originatorAccountName": "Yusuf Monday",
"originatorBankCode": "053",
"originatorBankName": "GTBank",
"beneficiaryAccountNumber": "78828882828",
"beneficiaryAccountName": "Dele Sam",
"beneficiaryBankCode": "044",
"beneficiaryBankName": "First Bank",
"createdAt": "2024-06-21T14:46:38.567849",
"updatedAt": "2024-06-21T15:30:10.291432"
}
],
"number": 0,
"sort": {
"empty": false,
"unsorted": false,
"sorted": true
},
"pageable": {
"pageNumber": 0,
"pageSize": 25,
"sort": {
"empty": false,
"unsorted": false,
"sorted": true
},
"offset": 0,
"unpaged": false,
"paged": true
},
"numberOfElements": 10,
"first": true,
"last": true,
"empty": false
},
"responseCode": "00"
}


# Transaction Summary

curl --location 'http://localhost:8080/api/v1/transactions/summary?searchDate=2024-06-21' \
--data ''

response:
{
"message": "Request Successful",
"data": {
"totalCount": 2,
"totalAmount": 700.00,
"totalBilledAmount": 696.50,
"totalCommission": 0.70,
"totalTransactionFee": 3.50
},
"responseCode": "00"
}





