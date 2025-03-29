package com.example.myapplication.ui.practice

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ReplyNavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER
}

enum class ReplyContentType {
    LIST_ONLY,
    LIST_AND_DETAILS
}

@Composable
fun ReplyApp(widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact, modifier: Modifier = Modifier) {
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection)
            )
    ) {

        val viewModel: ReplyViewModel = viewModel()
        val replyUiState = viewModel.uiState.collectAsState().value

        val navigationType: ReplyNavigationType
        val contentType: ReplyContentType

        when (widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
                contentType = ReplyContentType.LIST_ONLY
            }

            WindowWidthSizeClass.Medium -> {
                navigationType = ReplyNavigationType.NAVIGATION_RAIL
                contentType = ReplyContentType.LIST_ONLY
            }

            WindowWidthSizeClass.Expanded -> {
                navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
                contentType = ReplyContentType.LIST_AND_DETAILS
            }

            else -> {
                navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
                contentType = ReplyContentType.LIST_ONLY
            }
        }

        ReplyHomeScreen(
            navigationType = navigationType,
            contentType = contentType,
            replyUiState = replyUiState,
            onTabPressed = { mailboxType: MailboxType ->
                viewModel.updateCurrentMailbox(mailboxType = mailboxType)
                viewModel.resetHomeScreenStates()
            },
            onEmailCardPressed = { email: Email ->
                viewModel.updateDetailsScreenStates(
                    email = email
                )
            },
            onDetailScreenBackPressed = {
                viewModel.resetHomeScreenStates()
            },
            modifier = modifier
        )
    }
}

@Composable
fun ReplyHomeScreen(
    navigationType: ReplyNavigationType,
    contentType: ReplyContentType,
    replyUiState: ReplyUiState,
    onTabPressed: (MailboxType) -> Unit,
    onEmailCardPressed: (Email) -> Unit,
    onDetailScreenBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            mailboxType = MailboxType.Inbox,
            icon = Icons.Default.Inbox,
            text = stringResource(R.string.tab_inbox)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Sent,
            icon = Icons.AutoMirrored.Filled.Send,
            text = stringResource(R.string.tab_sent)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Drafts,
            icon = Icons.Default.Drafts,
            text = stringResource(R.string.tab_drafts)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Spam,
            icon = Icons.Default.Report,
            text = stringResource(R.string.tab_spam)
        )
    )

    if (navigationType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        val navigationDrawerContentDescription = stringResource(R.string.navigation_drawer)
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(Modifier.width(dimensionResource(R.dimen.drawer_width))) {
                    NavigationDrawerContent(
                        selectedDestination = replyUiState.currentMailbox,
                        onTabPressed = onTabPressed,
                        navigationItemContentList = navigationItemContentList,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                            .padding(dimensionResource(R.dimen.drawer_padding_content))
                    )
                }
            },
            modifier = Modifier
                .testTag(navigationDrawerContentDescription)
        ) {
            ReplyAppContent(
                navigationType = navigationType,
                contentType = contentType,
                replyUiState = replyUiState,
                onTabPressed = onTabPressed,
                onEmailCardPressed = onEmailCardPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        }
    } else {
        if (replyUiState.isShowingHomePage) {
            ReplyAppContent(
                navigationType = navigationType,
                contentType = contentType,
                replyUiState = replyUiState,
                onTabPressed = onTabPressed,
                onEmailCardPressed = onEmailCardPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        } else {
            ReplyDetailsScreen(
                replyUiState = replyUiState,
                isFullScreen = true,
                onBackPressed = onDetailScreenBackPressed,
                modifier = modifier
            )
        }
    }

}

@Composable
fun ReplyDetailsScreen(
    replyUiState: ReplyUiState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    BackHandler {
        onBackPressed()
    }
    Box(modifier = modifier) {
        LazyColumn(
            contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item {
                if (isFullScreen) {
                    ReplyDetailsScreenTopBar(
                        onBackButtonClicked = onBackPressed,
                        replyUiState = replyUiState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(R.dimen.topbar_padding_vertical),
                                bottom = dimensionResource(R.dimen.detail_topbar_padding_bottom)
                            )
                    )
                }
                ReplyEmailDetailsCard(
                    email = replyUiState.currentSelectedEmail,
                    mailboxType = replyUiState.currentMailbox,
                    isFullScreen = isFullScreen,
                    modifier = if (isFullScreen) {
                        Modifier
                            .navigationBarsPadding()
                            .padding(horizontal = dimensionResource(R.dimen.detail_card_outer_padding_horizontal))
                    } else {
                        Modifier
                            .padding(end = dimensionResource(R.dimen.detail_card_outer_padding_horizontal))
                    }
                )
            }
        }
    }
}

@Composable
private fun ReplyAppContent(
    navigationType: ReplyNavigationType,
    contentType: ReplyContentType,
    replyUiState: ReplyUiState,
    onTabPressed: (MailboxType) -> Unit,
    onEmailCardPressed: (Email) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        AnimatedVisibility(visible = navigationType == ReplyNavigationType.NAVIGATION_RAIL) {
            val navigationRailContentDescription = stringResource(R.string.navigation_rail)
            ReplyNavigationRail(
                currentTab = replyUiState.currentMailbox,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
                    .testTag(navigationRailContentDescription)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            if (contentType == ReplyContentType.LIST_AND_DETAILS) {
                ReplyListAndDetailContent(
                    replyUiState = replyUiState,
                    onEmailCardPressed = onEmailCardPressed,
                    modifier = Modifier.weight(1f)
                )
            } else {
                ReplyListOnlyContent(
                    replyUiState = replyUiState,
                    onEmailCardPressed = onEmailCardPressed,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = dimensionResource(R.dimen.email_list_only_horizontal_padding))
                )
            }
            AnimatedVisibility(visible = navigationType == ReplyNavigationType.BOTTOM_NAVIGATION) {
                val bottomNavigationContentDescription = stringResource(R.string.navigation_bottom)
                ReplyBottomNavigationBar(
                    currentTab = replyUiState.currentMailbox,
                    onTabPressed = onTabPressed,
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(bottomNavigationContentDescription)
                )
            }
        }
    }
}

@Composable
private fun ReplyDetailsScreenTopBar(
    onBackButtonClicked: () -> Unit,
    replyUiState: ReplyUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackButtonClicked,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.detail_topbar_back_button_padding_horizontal))
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(R.string.navigation_back)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = dimensionResource(R.dimen.detail_subject_padding_end))
        ) {
            Text(
                text = stringResource(replyUiState.currentSelectedEmail.subject),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReplyEmailDetailsCard(
    email: Email,
    mailboxType: MailboxType,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    val context = LocalContext.current
    val displayToast = { text: String ->
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.detail_card_inner_padding))
        ) {
            DetailsScreenHeader(
                email = email,
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (isFullScreen) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.detail_content_padding_top)))
            } else {
                Text(
                    text = stringResource(email.subject),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.detail_content_padding_top),
                        bottom = dimensionResource(R.dimen.detail_expanded_subject_body_spacing)
                    )
                )
            }
            Text(
                text = stringResource(email.body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DetailsScreenButtonBar(mailboxType, displayToast)
        }
    }
}

@Composable
private fun ReplyNavigationRail(
    currentTab: MailboxType,
    onTabPressed: (MailboxType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentTab == navItem.mailboxType,
                onClick = { onTabPressed(navItem.mailboxType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Composable
fun ReplyListOnlyContent(
    replyUiState: ReplyUiState,
    onEmailCardPressed: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    val emails = replyUiState.currentMailboxEmails

    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.email_list_item_vertical_spacing)
        )
    ) {
        item {
            ReplyHomeTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.topbar_padding_vertical))
            )
        }
        items(emails, key = {email -> email.id}) { email ->
            ReplyEmailListItem(
                email = email,
                selected = false,
                onCardClick = { onEmailCardPressed(email) }
            )
        }
    }

}

@Composable
private fun ReplyHomeTopBar(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ReplyLogo(
            modifier = Modifier
                .size(dimensionResource(R.dimen.topbar_logo_size))
                .padding(start = dimensionResource(R.dimen.topbar_logo_padding_start))
        )
        ReplyProfileImage(
            drawableResource = LocalAccountsDataProvider.defaultAccount.avatar,
            description = stringResource(R.string.profile),
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.topbar_profile_image_padding_end))
                .size(dimensionResource(R.dimen.topbar_profile_image_size))
        )
    }
}

@Composable
private fun ReplyLogo(
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.logo),
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
}

@Composable
private fun ReplyProfileImage(
    @DrawableRes drawableResource: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier.clip(CircleShape),
            painter = painterResource(drawableResource),
            contentDescription = description
        )
    }
}

@Composable
fun ReplyEmailListItem(
    email: Email,
    selected: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onCardClick
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.email_list_item_inner_padding))
        ) {
            ReplyEmailItemHeader(
                email,
                modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(email.subject),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.email_list_item_header_subject_spacing),
                    bottom = dimensionResource(R.dimen.email_list_item_subject_body_spacing)
                )
            )
            Text(
                text = stringResource(email.body),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ReplyListAndDetailContent(
    replyUiState: ReplyUiState,
    onEmailCardPressed: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    val emails = replyUiState.currentMailboxEmails

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LazyColumn(
            contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(R.dimen.email_list_only_horizontal_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.email_list_item_vertical_spacing))
        ) {
            items(emails, key = {email: Email -> email.id}) { email ->
                ReplyEmailListItem(
                    email = email,
                    selected = replyUiState.currentSelectedEmail.id == email.id,
                    onCardClick = { onEmailCardPressed(email) }
                )
            }
        }
        val activity = LocalContext.current as Activity
        ReplyDetailsScreen(
            replyUiState = replyUiState,
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.email_list_item_vertical_spacing))
                .weight(1f),
            onBackPressed = { activity.finish() }
        )
    }
}

@Composable
private fun ReplyEmailItemHeader(email: Email, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ReplyProfileImage(
            drawableResource = email.sender.avatar,
            description = stringResource(email.sender.firstName) + " " + stringResource(email.sender.lastName),
            modifier = Modifier.size(dimensionResource(R.dimen.email_header_profile_size))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = dimensionResource(R.dimen.email_header_content_padding_horizontal),
                    vertical = dimensionResource(R.dimen.email_header_content_padding_vertical)
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(email.sender.firstName),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(email.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun DetailsScreenHeader(email: Email, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ReplyProfileImage(
            drawableResource = email.sender.avatar,
            description = stringResource(email.sender.firstName) + " " + stringResource(email.sender.lastName),
            modifier = Modifier
                .size(dimensionResource(R.dimen.email_header_profile_size))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    vertical = dimensionResource(R.dimen.email_header_content_padding_vertical),
                    horizontal = dimensionResource(R.dimen.email_header_content_padding_horizontal)
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(email.sender.firstName),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(email.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun DetailsScreenButtonBar(
    mailboxType: MailboxType,
    displayToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (mailboxType) {
            MailboxType.Drafts ->
                ActionButton(
                    text = stringResource(R.string.continue_composing),
                    onButtonClick = displayToast
                )

            MailboxType.Spam ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.detail_button_bar_padding_vertical)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.detail_button_bar_item_spacing))
                ) {
                    ActionButton(
                        text = stringResource(R.string.move_to_inbox),
                        onButtonClick = displayToast,
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = stringResource(R.string.delete),
                        onButtonClick = displayToast,
                        containIrreversibleAction = true,
                        modifier = Modifier.weight(1f)
                    )
                }

            MailboxType.Sent, MailboxType.Inbox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.detail_button_bar_padding_vertical)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.detail_button_bar_item_spacing))
                ) {
                    ActionButton(
                        text = stringResource(R.string.reply),
                        onButtonClick = displayToast,
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = stringResource(R.string.reply_all),
                        onButtonClick = displayToast,
                        modifier = Modifier.weight(1f)
                    )
                }
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    containIrreversibleAction: Boolean = false
) {
    Box(modifier = modifier) {
        Button(
            onClick = { onButtonClick(text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.detail_action_button_padding_vertical)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (containIrreversibleAction)
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = text,
                color = if (containIrreversibleAction)
                    MaterialTheme.colorScheme.onError
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReplyBottomNavigationBar(
    currentTab: MailboxType,
    onTabPressed: (MailboxType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.mailboxType,
                onClick = { onTabPressed(navItem.mailboxType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    selectedDestination: MailboxType,
    onTabPressed: (MailboxType) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    Column(modifier =  modifier) {
        NavigationDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.profile_image_padding))
        )
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                selected = selectedDestination == navItem.mailboxType,
                label = {
                    Text(
                        text = navItem.text,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header)))
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { onTabPressed(navItem.mailboxType) }
            )
        }
    }
}

@Composable
private fun NavigationDrawerHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReplyLogo(modifier = Modifier.size(dimensionResource(R.dimen.reply_logo_size)))
        ReplyProfileImage(
            drawableResource = LocalAccountsDataProvider.defaultAccount.avatar,
            description = stringResource(R.string.profile),
            modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
        )
    }
}

private data class NavigationItemContent(
    val mailboxType: MailboxType,
    val icon: ImageVector,
    val text: String
)

data class ReplyUiState(
    val mailboxes: Map<MailboxType, List<Email>> = emptyMap(),
    val currentMailbox: MailboxType = MailboxType.Inbox,
    val currentSelectedEmail: Email = LocalEmailsDataProvider.defaultEmail,
    val isShowingHomePage: Boolean = true
) {
    val currentMailboxEmails: List<Email> by lazy { mailboxes[currentMailbox]!! }
}

class ReplyViewModel : ViewModel() {

    private  val _uiState = MutableStateFlow(ReplyUiState())
    val uiState: StateFlow<ReplyUiState> = _uiState.asStateFlow()

    init {
        initializeUiState()
    }

    private fun initializeUiState() {
        val mailboxes: Map<MailboxType, List<Email>> =
            LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
        _uiState.value =
            ReplyUiState(
                mailboxes = mailboxes,
                currentSelectedEmail = mailboxes[MailboxType.Inbox]?.get(0)
                    ?: LocalEmailsDataProvider.defaultEmail
            )
    }

    fun updateDetailsScreenStates(email: Email) {
        _uiState.update {
            it.copy(
                currentSelectedEmail = email,
                isShowingHomePage = false
            )
        }
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                currentSelectedEmail = it.mailboxes[it.currentMailbox]?.get(0)
                    ?: LocalEmailsDataProvider.defaultEmail,
                isShowingHomePage = true
            )
        }
    }

    fun updateCurrentMailbox(mailboxType: MailboxType) {
        _uiState.update {
            it.copy(
                currentMailbox = mailboxType
            )
        }
    }

}

//local account data
object LocalAccountsDataProvider {
    val defaultAccount = Account(-1, -1, -1, -1, R.drawable.avatar_1)

    private val allUserContactAccount = listOf(
        Account(
            id = 4L,
            firstName = R.string.account_4_first_name,
            lastName = R.string.account_4_last_name,
            email = R.string.account_4_email,
            avatar = R.drawable.avatar_1
        ),
        Account(
            id = 5L,
            firstName = R.string.account_5_first_name,
            lastName = R.string.account_5_last_name,
            email = R.string.account_5_email,
            avatar = R.drawable.avatar_3
        ),
        Account(
            id = 6L,
            firstName = R.string.account_6_first_name,
            lastName = R.string.account_6_last_name,
            email = R.string.account_6_email,
            avatar = R.drawable.avatar_5
        ),
        Account(
            id = 7L,
            firstName = R.string.account_7_first_name,
            lastName = R.string.account_7_last_name,
            email = R.string.account_7_email,
            avatar = R.drawable.avatar_0
        ),
        Account(
            id = 8L,
            firstName = R.string.account_8_first_name,
            lastName = R.string.account_8_last_name,
            email = R.string.account_8_email,
            avatar = R.drawable.avatar_7
        ),
        Account(
            id = 9L,
            firstName = R.string.account_9_first_name,
            lastName = R.string.account_9_last_name,
            email = R.string.account_9_email,
            avatar = R.drawable.avatar_express
        ),
        Account(
            id = 10L,
            firstName = R.string.account_10_first_name,
            lastName = R.string.account_10_last_name,
            email = R.string.account_10_email,
            avatar = R.drawable.avatar_2
        ),
        Account(
            id = 11L,
            firstName = R.string.account_11_first_name,
            lastName = R.string.account_11_last_name,
            email = R.string.account_11_email,
            avatar = R.drawable.avatar_8
        ),
        Account(
            id = 12L,
            firstName = R.string.account_12_first_name,
            lastName = R.string.account_12_last_name,
            email = R.string.account_12_email,
            avatar = R.drawable.avatar_6
        ),
        Account(
            id = 13L,
            firstName = R.string.account_13_first_name,
            lastName = R.string.account_13_last_name,
            email = R.string.account_13_email,
            avatar = R.drawable.avatar_4
        )
    )

    fun getContactAccountById(accountId: Long): Account {
        return allUserContactAccount.firstOrNull { it.id == accountId }
            ?: allUserContactAccount.first()
    }
}
//local email data
object LocalEmailsDataProvider {
    val defaultEmail = Email(id = -1, sender = LocalAccountsDataProvider.defaultAccount)

    val allEmails = listOf(
        Email(
            id = 0L,
            sender = LocalAccountsDataProvider.getContactAccountById(9L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_0_subject,
            body = R.string.email_0_body,
            createdAt = R.string.email_1_time
        ),
        Email(
            id = 1L,
            sender = LocalAccountsDataProvider.getContactAccountById(6L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_1_subject,
            body = R.string.email_1_body,
            createdAt = R.string.email_1_time,
        ),
        Email(
            2L,
            LocalAccountsDataProvider.getContactAccountById(5L),
            listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_2_subject,
            body = R.string.email_2_body,
            createdAt = R.string.email_2_time,
        ),
        Email(
            3L,
            LocalAccountsDataProvider.getContactAccountById(8L),
            listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_3_subject,
            body = R.string.email_3_body,
            createdAt = R.string.email_3_time,
            mailbox = MailboxType.Sent,
        ),
        Email(
            id = 4L,
            sender = LocalAccountsDataProvider.getContactAccountById(11L),
            subject = R.string.email_4_subject,
            body = R.string.email_4_body,
            createdAt = R.string.email_4_time,
        ),
        Email(
            id = 5L,
            sender = LocalAccountsDataProvider.getContactAccountById(13L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_5_subject,
            body = R.string.email_5_body,
            createdAt = R.string.email_5_time,
        ),
        Email(
            id = 6L,
            sender = LocalAccountsDataProvider.getContactAccountById(10L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_6_subject,
            body = R.string.email_6_body,
            createdAt = R.string.email_6_time,
            mailbox = MailboxType.Sent,
        ),
        Email(
            id = 7L,
            sender = LocalAccountsDataProvider.getContactAccountById(9L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_7_subject,
            body = R.string.email_7_body,
            createdAt = R.string.email_7_time,
        ),
        Email(
            id = 8L,
            sender = LocalAccountsDataProvider.getContactAccountById(13L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_8_subject,
            body = R.string.email_8_body,
            createdAt = R.string.email_8_time,
        ),
        Email(
            id = 9L,
            sender = LocalAccountsDataProvider.getContactAccountById(10L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_9_subject,
            body = R.string.email_9_body,
            createdAt = R.string.email_9_time,
            mailbox = MailboxType.Drafts,
        ),
        Email(
            id = 10L,
            sender = LocalAccountsDataProvider.getContactAccountById(5L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_10_subject,
            body = R.string.email_10_body,
            createdAt = R.string.email_10_time,
        ),
        Email(
            id = 11L,
            sender = LocalAccountsDataProvider.getContactAccountById(5L),
            recipients = listOf(LocalAccountsDataProvider.defaultAccount),
            subject = R.string.email_11_subject,
            body = R.string.email_11_body,
            createdAt = R.string.email_11_time,
            mailbox = MailboxType.Spam,
        )
    )

    fun get(id: Long): Email? {
        return allEmails.firstOrNull { it.id == id }
    }
}

//data account
data class Account(
    val id: Long,
    @StringRes val firstName: Int,
    @StringRes val lastName: Int,
    @StringRes val email: Int,
    @DrawableRes val avatar: Int,
)
//data mailbox
enum class MailboxType {
    Inbox,
    Drafts,
    Sent,
    Spam
}
//data email
data class Email(
    val id: Long,
    val sender: Account,
    val recipients: List<Account> = emptyList(),
    @StringRes val subject: Int = -1,
    @StringRes val body: Int = -1,
    var mailbox: MailboxType = MailboxType.Inbox,
    var createdAt: Int = -1
)

@Preview(showBackground = true, widthDp = 300)
@Composable
fun ReplyAppCompactPreview() {

        Surface {
            ReplyApp(widthSizeClass = WindowWidthSizeClass.Compact)
        }

}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun ReplyAppMediumPreview() {

        Surface {
            ReplyApp(widthSizeClass = WindowWidthSizeClass.Medium)
        }

}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ReplyAppExpandedPreview() {

        Surface {
            ReplyApp(widthSizeClass = WindowWidthSizeClass.Expanded)

    }
}