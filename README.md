<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->





<br />



  <h3 align="center">Multi-Threaded Drawing Board</h3>

  <p align="center">
    An awesome README template to jumpstart your projects!
    <br />
    <a href="https://github.com/pt4300/Multi-threaded-Dictionary-Server.git"><strong>Explore the docs »</strong></a>
    <br />


  </p>

</div>





<!-- ABOUT THE PROJECT -->

## About The Project


The Multi-Threaded Drawing Board is a real-time collaborative whiteboard system, designed for seamless remote drawing and communication.

**Key Features**:
- **Drawing Tools**: Users can draw shapes and add text on a shared digital canvas.
- **Chat Window**: Allows for real-time text communication among users.
- **User Management (Admin)**: Accept/reject client requests; remove users.
- **File Management (Admin)**: Save, load, and close the canvas in JSON format.
- **Canvas Control (Admin)**: Clear or reset the canvas.

**Technical Highlights**:
- **Java's RMI**: Facilitates communication between remote Java objects.
- **Multi-Threaded**: Efficiently handles multiple user interactions.
- **Real-Time Interactivity**: Immediate reflection of changes on the shared canvas.
- **Thread Safety**: Ensures data integrity in a concurrent environment.

**Components**:
- **RMI Package**: Server-side RMI interface and implementation.
- **UI Package**: User interface and event listeners.
- **Utility/User Package**: Maintains client status and captures shape/message data.

**Application**: Ideal for remote work and learning environments, offering an interactive and concurrent drawing and communication platform.

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

* jdk 17 - preferred Eclipse Temurin 17.0.6
* google gson library - only for source code build

### Installation

1. Clone the repo

   ```sh
   git clone https://github.com/pt4300/Multi-Threaded-Drawing-Board.git
   ```

2. Install jdk 17

3. For running jar file, please extract the client.jar and server.jar and cd to the extracted folder



<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

### Starting the Server:

1. **Launch the Server**:    ```sh
   java server <serverIPAddress> <serverPort> username
   ```
   Default IP on localmachine - localhost

2. **User Management**: As a manager on the server-side, you can accept or reject client requests to join the session. Additionally, you have the power to remove a participant if needed.

3. **File Management**: As a manager, you can save the current state of the whiteboard to a JSON file, load a previous state from a JSON file, or close the current file. Loading will synchronize the whiteboard across all connected clients.

4. **Canvas Management**: As a manager, you can clear the canvas, removing all current drawings. This will also clear the canvas for all connected clients.

### Client Application:

1. **Connect to the Server**: 
	```sh
	java client <serverIPAddress> <serverPort> username
	```
	serverPort needs be align with server port number
2. **Drawing Tools**:
    - **Select Color**: Press the "Color" button to choose a drawing color.
    - **Select Shape**: Choose a shape (Line, Oval, Rectangle) or text.
    - **Draw on Whiteboard**: Click and drag on the whiteboard to draw the selected shape. Release the mouse button to finalize the drawing.
    - The drawings are synchronized in real-time across all clients.

3. **Chat Window**:
    - **Send Messages**: Type messages in the chat input area and hit Enter or click the Send button to communicate with other participants in real-time.

4. **Disconnect**: To leave the session, simply close the client application. Your drawings will remain on the whiteboard for the other connected clients unless the manager clears the canvas or loads a different file.

### Notes:

- Ensure that the server application is running before trying to connect using the client application.
- Be cautious when using the "Clear" button as a manager, as it clears the whiteboard for all users.
- Real-time updates are handled by dedicated threads for drawing actions, user management, and message exchanges.
- All file saving/loading is done in JSON format.
- If experiencing lag or delay in updates, it might be due to network latency or high server load, especially with many clients or complex drawings.

By following this guide, users can effectively make use of the Multi-Threaded-Drawing-Board application for collaborative drawing and interaction on a shared digital canvas.

### Screenshot:
 <figure>
   <img src="images\server.png" alt="Server GUI">
   <figcaption>Server Interface</figcaption>
     </figure>

 <figure>
   <img src="images\client_login.png" alt="Client Login Interface">
   <figcaption>Client Login Interface</figcaption>
     </figure>

 <figure>
   <img src="images\client.png" alt="Client GUI">
   <figcaption>Client Interface</figcaption>
     </figure>

 <figure>
   <img src="images\admin_approval2.png" alt="User management GUI">
   <figcaption>Admin approval Interface</figcaption>
     </figure>
​    

<p align="right">(<a href="#readme-top">back to top</a>)</p>






<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->

## Contact

Your Name - pt4300@hotmail.com

Project Link: [https://github.com/pt4300/Multi-threaded-Dictionary-Server](https://github.com/pt4300/Multi-threaded-Dictionary-Server)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

