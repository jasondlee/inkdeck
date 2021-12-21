Currently just a dumping ground.

   Network conversation:
   - A client connects to the server and asks for a list of games.
   - Once a game is selected, client informs the server. The server associates the client socket with the game for
     bidirectional communication.
     - The client can optionally create a new game
   - Once game owner is satisfied that all players are connected, the game is started
     - The deck is shuffled
     - The initial state of the deck is sent to all connected clients
   - Each player is presented with the current state of play piles (initially empty)
     - Each player plays one card on one stack. These decisions are held locally for now.
     - Once the minimum number of cards have been played and the player decides to end his/her turn, the player indicates
       the decision using the provided user interface.
     - The client draws from the draw pile the number of cards played on the piles.
     - A message is created indicating what cards/numbers were played on which pile.
     - The message is sent to the server, which relays it to the connected clients.
     - Each client pops and discards the number of cards played from their local copy of the draw pile to keep the state
       current
     - The next player is prompted to play his/her round.
   - If the player is unable to play the required number of cards.
     - If he/she is unable to do so, the player indicates this by use of the provided user interface, and the Game End
       message is sent
     - The server relays this message to each client
     - Each client reports back with the $MESSAGE to indicate the number of cards remaining
     - The server collects these messages and sends to each client the Game Summary message, which includes the number
       of cards remaining
   Needed:
     - Client disconnect/reconnect handling. Assume the best for now.
   Requests:
   - List games
   - Create game
   - Join game
   - Start game
   - Play round
   - Round played
   - End game
