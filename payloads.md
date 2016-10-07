## 1 - New Customer
## 2 - New Visit
## 3 - Search customer by payload
## 4 - Get Customer by Id

## 1. New customer payload (create with email)
CRM new customer
- validate if email exists? if so, get cust id
- if not exist, create a new cust id (incr by 1)
When you create a new customer it goes to the "custom profile page" with the payload you just used. 
This does not use "customer search by id" because it might not have been created yet.

```
{
    name:dora
    license:lin123
    etc
}
```

## 2. New visit payload (create with id)
new customer -> [adapter] getcustomerdetails with plate -> customer page -> new visit w/cust id
"Cache" this when going back to the main profile page. Use the payload you just created, not do a "search customer by id" right? ??

CRM new visit
- get id by email, then create new visit using that id


New visits payload - does not have email only cust id as a foreign key
{id}/visits
```
{
    date:12/13/16,
    type: oil change,
    comment:help    
}
```

1. New visit - post to CRM 
2. CRM uses email to look for custid in the CRM (tcheng@us.ibm.com has id=1) then uses that id to 3. create the visit record in the visits table using the below payload.

```
{
    id: 1,
    date:12/13/16,
    type: oil change,
    comment:help    
}
``

## 3. Search by name/license/vin
Search Payload:

```
{
    type: name,
    text: dora
}

```

Returns:
```
{ 
    customers: 
        {
            id:1
            name:dora,
            license:lin123,
        },
        {
            id:2,
            name:dora,
            license:lin123,
        }
}
```

## 4. Customer get by licenseplates:
Done when transitioning from search page to customer info page
This is 2 queries on dashdb (Get customer by id, get visits by id, then the json object is formed)

Search payload:
```
{
    licenseplate:112345
}
```


Returns:
```
{
    id:1
    email:123@yahoo.com
    name:dora,
    license:lin123,
    visits:
            {
                {
                    date:12/13/16,
                    type: oil change,
                    comments:help
                },
                {
                    date:12/13/16,
                    type: oil change,
                    comments:help
                }
            }
}
```            

Scenario
1. new customer
2. customer info
