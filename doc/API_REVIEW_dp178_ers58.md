#### What about your API/design is intended to be flexible?

* Entities, events, and behaviors should all be interfaces so that each type of each is very extensible. You should be able to add attributes to each without changing existing code or the hierarchy.
* My Player team's APIs are primarily intended for communication between different parts of the game and make it so that communication is easily achieved with limited dependencies.

#### How is your API/design encapsulating your implementation decisions?

* Standardizing communication between the four subparts of the project is very important, and my subteam's APIs support that. The 4 APIs that GamePlayer has makes it so 
  that each subteam can independently work on their own parts with limited overarching dependencies which is useful for integration and debugging. We both use our APIs to 
  keep our subteam's code closed to other parts.
* Elizabeth does not have an API but that is reasonable because the work that the Authoring team does should be kept mostly within itself and independent of
  what happens in the rest of the game. This might change as the project goes on as we look at issues such as game modification during gamepplay, which could
  present an opportunity to use interfaces.

#### How is your part linked to other parts of the project?

* Elizabeth: Authoring works really closely with engine because they allow users to instantiate and modify classes created by engine. They also work closely with  
  data which serves as a link between Authoring and Player. Authoring also work s with Player to standardize how data sends and receives information. Otherwise,
  authoring is pretty self-contained. 

* Dana: Player works most closely with Data that serves as a bridge between Player and Authoring. We also rely on Engine to add functionality to Player.

#### What exceptions (error cases) might occur in your part and how will you handle them (or not, by throwing)?

* Elizabeth: There should not be exceptions or error cases because they are restricting UI to account for exceptions/errors.
* Dana: We don't expect errors in our API because we are limiting what the user can select based on what we are reading in from Data.

#### Why do you think your API/design is good (also define what your measure of good is)?

* Elizabeth: They prioritized standardizing communication between all 4 branches, and having an API is a nice way to be transparent about communication.
* Dana: Our API makes communication with Data super easy and also allows for easy updating of Player information.

#### Discuss the use cases/issues that you are responsible for: are they descriptive, appropriate, reasonably sized?

#### Estimate how long you think each will take and why. What, if anything, makes estimating these tasks uncertain?

#### What feature/design problem are you most excited to work on?

#### What major feature do you plan to implement this weekend?