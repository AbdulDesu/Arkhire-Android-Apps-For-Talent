package com.sizdev.arkhirefortalent.homepage.item.home.project

import com.sizdev.arkhirefortalent.networking.ArkhireApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProjectPresenter(private val coroutineScope: CoroutineScope,
                       private val service: ArkhireApiService?) : ProjectContract.Presenter {

    private var view: ProjectContract.View? = null

    override fun bindToView(view: ProjectContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getAllProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllProjectResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.setError("Project Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectResponse) {
                if (result.success) {
                    val list = result.data?.map{
                        ProjectModel(it.contributorID, it.talentID, it.companyID, it.companyName, it.companyImage, it.projectTittle, it.projectDesc, it.projectDuration, it.projectSalary, it.projectImage, it.offeringID, it.hiringStatus, it.offeredSalary, it.replyMsg)
                    }
                    view?.addProjectList(list)
                    view?.hideProgressBar()

                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getHighlightProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getNewerProjectResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.setError("Project Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectResponse) {
                if (result.success) {
                    val list = result.data?.map{
                        ProjectModel(it.contributorID, it.talentID, it.companyID, it.companyName, it.companyImage, it.projectTittle, it.projectDesc, it.projectDuration, it.projectSalary, it.projectImage, it.offeringID, it.hiringStatus, it.offeredSalary, it.replyMsg)
                    }
                    view?.addProjectList(list)
                    view?.hideProgressBar()

                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getApprovedProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getApprovedProjectResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.setError("Project Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectResponse) {
                if (result.success) {
                    val list = result.data?.map{
                        ProjectModel(it.contributorID, it.talentID, it.companyID, it.companyName, it.companyImage, it.projectTittle, it.projectDesc, it.projectDuration, it.projectSalary, it.projectImage, it.offeringID, it.hiringStatus, it.offeredSalary, it.replyMsg)
                    }
                    view?.addProjectList(list)
                    view?.hideProgressBar()

                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getDeclinedProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDeclinedProjectResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.setError("Project Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectResponse) {
                if (result.success) {
                    val list = result.data?.map{
                        ProjectModel(it.contributorID, it.talentID, it.companyID, it.companyName, it.companyImage, it.projectTittle, it.projectDesc, it.projectDuration, it.projectSalary, it.projectImage, it.offeringID, it.hiringStatus, it.offeredSalary, it.replyMsg)
                    }
                    view?.addProjectList(list)
                    view?.hideProgressBar()

                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

    override fun getWaitingProject(accountID: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getWaitingProjectResponse(accountID)
                } catch (e: HttpException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 403 -> {
                                view?.setError("Session Expired !")
                            }

                            e.code() == 404 -> {
                                view?.setError("Project Not Found !")
                            }

                            else -> {
                                view?.setError("Unknown Error, Please Try Again Later !")
                            }
                        }
                    }
                }
            }

            if (result is ProjectResponse) {
                if (result.success) {
                    val list = result.data?.map{
                        ProjectModel(it.contributorID, it.talentID, it.companyID, it.companyName, it.companyImage, it.projectTittle, it.projectDesc, it.projectDuration, it.projectSalary, it.projectImage, it.offeringID, it.hiringStatus, it.offeredSalary, it.replyMsg)
                    }
                    view?.addProjectList(list)
                    view?.hideProgressBar()

                } else {
                    view?.hideProgressBar()
                    view?.setError(result.message)
                }
            }
        }
    }

}