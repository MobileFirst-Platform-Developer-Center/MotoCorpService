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
    email:143@yahoo.com,
    name:dora
    license:lin123
    etc
}
```

## 2. New visit payload (create with email)
"Cache" this when going back to the main profile page. Use the payload you just created, not do a "search customer by id" right? ??

CRM new visit
- get id by email, then create new visit using that id

```
{
    email: 123@yahoo.com,
    date:12/13/16,
    type: oil change,
    comment:help    
}
```


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
            email:123@yahoo.com
            name:dora,
            license:lin123,
        },
        {
            id:2,
            email:123@yahoo.com
            name:dora,
            license:lin123,
        }
 
}
```

## 4. Customer get by id:
Done when transitioning from search page to customer info page

Search payload:
```
{
    id:1
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
                    comements:help
                },
                {
                    date:12/13/16,
                    type: oil change,
                    comements:help
                }
            }
}
```            

