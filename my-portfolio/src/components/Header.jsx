import React from 'react'
import porfileImage from '../assets/kavya.png'
import '../assets/styles/Header.css'

const Header = () => {
  return (
    <>
   <div className="header">
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
  <div className="header__container">
    <div className="header__container--top">
      <h1 className="header__container--top--title">Srinivasa Kavya Yadav</h1>
      <p className="header__container--top--subtitle">Software Engineer | Full Stack Senior Developer | Tech Enthusiast</p>
    </div>
    <div className="header__container--bottom">
      <p className="header__container--bottom--text">Welcome to my portfolio! Explore my projects and skills.</p>
    </div>
</div>
</div>

      </>
  )
}

export default Header
