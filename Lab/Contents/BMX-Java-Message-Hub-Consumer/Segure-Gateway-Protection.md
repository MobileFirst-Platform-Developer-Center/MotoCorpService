```
- Enter your Secure gateway destination
- Enable protection
- Collect credentials
- Update credentials on your java consumer yml
- Push
> If this was already pushed before, update via bluemix console, to ensure new values are reflected on the app
- Restart the java consumer
-
```



##### Edit a Destination
> - Enter in your gateway on the Service Dashboard.
> - Click on Add a destination
> - Select "On-Premesis" -> This will create a destination from a bluemix endpoint to a service on your OnPrem network.
> - Press Next
> - Provide the Resource Hostname and Port of the Resource you want to expose to bluemix.
> - Select the protocol **TCP** and press Next
> - On **What kind of authentication does your destination enforce?** select **None** and then press next
> - On **If you would like to make your destination private, add IP table rules below** you don't have fill this values for now, just press next.  
> - On **"What would you like to name this destination?"** provide a name for your destination, eg: "My OnPrem Server". Then press "finish"
