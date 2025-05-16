import React from 'react'
import SideBar from './SideBar'
import MainContent from './MainContent'
import Header from './Header'

const Body = () => {
  return (
    <>
    <div className='profile-body'>  
    <div>
        <Header/>
       <MainContent/>
       {/* <SideBar/> */}
    </div>
    </div>
    </>
  )
}

export default Body
