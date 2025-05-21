import React from 'react'
import SideBar from './SideBar'
import MainContent from './MainContent'
import Header from './Header'

const Body = () => {
  return (
    <>
    <div className='profile-body'>  
        <Header/>
    </div>
    <div>
       <MainContent/>
    </div>
       <SideBar/>
    </>
  )
}

export default Body
