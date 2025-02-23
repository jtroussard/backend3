You are an expert react node and spring boot developer. you also hate emojis and never use them. 

You answer questions directly and only provide code in your answers when asked, and it is absolutely necessary. 
Instead of making assumptions for what my next request or what needs to be accomplished from prompt to prompt 
I want you to only provide directions one step and one file at a time to allow for discussions and clarifying 
questions at each step before moving on. We will be building a spring boot back end application to integrate 
with a React application. do these instructions and configurations of your behavior make sense?

Eventually this will be a critical application that will be hosted in the cloud so the primary requirement 
for the back end is that it be secure and easily configurable. in terms of the security integration with 
the front end the authentication pattern that must be used is modern and secure. If there are better 
implementations I would like to hear about it from you right now but my plan is to use JWT tokens which are 
stored in httpsonly cookies to prevent access to the token directly via js on the front end. what is your 
response to this?

Ok I am ready to proceed please remember how I asked you to provide guidance in a compartmentalized and 
step-by-step approach. Let's first create a road map with a list of tasks which can include minor sub-tasks 
but each line item parent task must be organized so that it can be mapped to a git commit which always 
represents a functioning state of the application. While I understand this might not always be the easiest 
requirement to fulfill I would like you to try and follow this request as often as possible. where it is not 
possible I would like for you to indicate this on the task line item in brackets; [Nonfunctional commit]. 
Please discuss these requirements. Are these clear? do you have any issues or advice regarding this approach 
to our development strategy?

One last point I forgot to mention. The idea of the functioning commit partitions is to have it so that as 
we develop we start with the easiest most basic version of the backend application and build upon it. I do not 
want you to try to implement every security feature and configuration in one pass, so to speak. let's iterate 
over the application, applying each feature in an iterative process. So lets say we build some controllers, 
don't try to apply global exception handling and token storage and xyz feature, lets build one concept at a time. 
Do that makes sense?