# CoderDojoAndroidApp
This is a readme file. CoderDojoAndroidApp is now Lightning presentation waitlist. This is how life works.

Website is at CoderDojoLeaderboard.mybluemix.net

Website is up! After a few hours of late-in-the-evening research, I finally got that great feeling when something finally works. I figured out how to get Bluemix to run the website.

Basic outline (discuss here if you want, ask questions):
--------------

- The Bluemix app is NOT IN ANY WAY synced to the GitHub repository
- instead, to run the application, I pull the repository onto my computer with git, then I push the application to Bluemix using cf (the Cloud Foundry command-line application).
- The way Cloud Foundry works, it runs an "application." What the Bluemix website doesn't tell you with its mess of menus is that you don't have to run a server-side rendering application like Node.js. You can run any application theoretically, but in our case, we just want a web server. You can, in fact, do that.
- Some extensive googling later, I found a "buildpack" (a buildpack is basically cf's name for a server-side application) for a simple Nginx web server.
- https://github.com/cloudfoundry/staticfile-buildpack
- So, to sum it all up, if I want to start running the server, I cd into my local copy of the git repo, and run (assuming I've already configured the Bluemix server parameters and IBM id in cf):
	- cf push -m 64M -b https://git	hub.com/cloudfoundry/staticfile-buildpack CoderDojoLeaderboard
- I won't always keep the website live, but since I have a Bluemix trial, I think I can use as much bandwidth as I want. Since it's only 64 MB of server RAM, I'll keep it running until our next meeting if anyone wants to check it out. In fact, 64 MB * 744 hours in a 31-day month is only 46.5 GB-hours, and Bluemix lets you have 375 GB-hours a month free even after your trial is over, so either way I'm fine running the app the entire time.


Note: If you pushed something to the repository and want me to restart the web server, send me an email.
