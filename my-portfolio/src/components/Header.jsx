import React from 'react'
import porfileImage from '../assets/kavya.png'
import '../assets/styles/Header.css'

const Header = () => {
  return (
    <>
   <div className="header" style={{ backgroundColor: 'white' }}>
  <header className="portfolio-header">
    {/* <div className="header-left"> */}
        <div className="header-logo">
          <img src={porfileImage} alt="Logo" className="profile-img" />
        </div>
      <div className="logo-container">
        <h1 className="logo">SKY
          {/* <p>Kavya</p> */}
          {/* <p>Srinivasa</p> */}
        </h1>
      </div>

      <nav className="header-right">
        <ul className="nav-links">
          <li><a href="#skills">Skills</a></li>
          <li><a href="#blogs">Blogs/Articles</a></li>
          <li><a href="#projects">Projects</a></li>
          <li><a href="#contact">Contact</a></li>
          <li><a href="#youtube">YouTube</a></li>
        </ul>
      </nav>
    {/* </div> */}
  </header>
</div>

</>
  )
}

export default Header
